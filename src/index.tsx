import TiktokBiz from './NativeTiktokBiz';

function initializeTikTokSDK(
  appId: string,
  tiktokAppId: string
): Promise<boolean> {
  return TiktokBiz.initializeTikTokSDK(appId, tiktokAppId);
}

function trackEvent(
  eventName: string,
  properties: Record<string, any>
): Promise<boolean> {
  return TiktokBiz.trackEvent(eventName, properties);
}

export default {
  initializeTikTokSDK,
  trackEvent,
};
