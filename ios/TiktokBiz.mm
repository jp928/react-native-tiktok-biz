#import "TiktokBiz.h"
#import <TikTokBusinessSDK/TikTokBusinessSDK.h>

@implementation TiktokBiz
RCT_EXPORT_MODULE()

// Initialize TikTok SDK
- (void)initializeTikTokSDK:(NSString *)appId 
tiktokAppId:(NSString *)tiktokAppId
resolve:(RCTPromiseResolveBlock)resolve 
reject:(RCTPromiseRejectBlock)reject {
  TikTokConfig *config = [[TikTokConfig alloc] initWithAppId:appId tiktokAppId:tiktokAppId];
  
  [config setAutomaticTrackingEnabled: true];
  [config setTrackingEnabled: true];
  [config setInstallTrackingEnabled: true];
  
  // Initialize the SDK with completion handler
  [TikTokBusiness initializeSdk:config completionHandler:^(BOOL success, NSError * _Nullable error) {
    if (!success) { // initialization failed
      NSLog(@"%@", error.localizedDescription);
      reject(@"tiktok_init_failed", error.localizedDescription, error);
    } else { // initialization successful
      resolve(@(YES));
    }
  }];
}

// Track events
- (void)trackEvent:(NSString *)eventName 
        properties:(NSDictionary *)properties
           resolve:(RCTPromiseResolveBlock)resolve 
            reject:(RCTPromiseRejectBlock)reject {
  @try {
    TikTokBaseEvent *achieveLevelEvent = [TikTokBaseEvent eventWithName:eventName];
    
    for (NSString *key in properties) {
      [achieveLevelEvent addPropertyWithKey:key value:properties[key]];
    }
    [TikTokBusiness trackTTEvent:achieveLevelEvent];
    [TikTokBusiness explicitlyFlush];
    resolve(@(YES));
  } @catch (NSException *exception) {
    reject(@"tiktok_track_failed", exception.reason, nil);
  }
}

- (std::shared_ptr<facebook::react::TurboModule>)getTurboModule:
(const facebook::react::ObjCTurboModule::InitParams &)params
{
  return std::make_shared<facebook::react::NativeTiktokBizSpecJSI>(params);
}

@end
