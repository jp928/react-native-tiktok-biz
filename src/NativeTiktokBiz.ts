import type { TurboModule } from 'react-native';
import { TurboModuleRegistry } from 'react-native';

export interface Spec extends TurboModule {
  initializeTikTokSDK(appId: string, tiktokAppId: string): Promise<boolean>;
  trackEvent(
    eventName: string,
    properties: { [key: string]: string | number | boolean }
  ): Promise<boolean>;
}

export default TurboModuleRegistry.getEnforcing<Spec>('TiktokBiz');
