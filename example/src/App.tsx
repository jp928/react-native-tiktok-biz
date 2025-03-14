import { useEffect } from 'react';
import { View, StyleSheet, Button } from 'react-native';
import TiktokBusiness from 'react-native-tiktok-biz';

export default function App() {
  useEffect(() => {
    const initializeTikTokSDK = async () => {
      try {
        const result = await TiktokBusiness.initializeTikTokSDK(
          '12345',
          '12345'
        );

        console.log(result);
      } catch (error) {
        console.log(error);
      }
    };

    initializeTikTokSDK();
  }, []);

  return (
    <View style={styles.container}>
      <Button
        title="Initialize TikTok SDK"
        onPress={async () => {
          const result = await TiktokBusiness.trackEvent('Button Clicked', {
            test_property: 'test_value',
          });
          console.log(result);
        }}
      />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: 'blue',
  },
});
