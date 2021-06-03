import React from 'react';
import {ImageBackground, View} from 'react-native';
import {Container, Button, H2, Text} from 'native-base';
import styles from './style';
const launchscreenBg = require('../../assets/splashScreen.png');

export default function Splash({navigation}) {
  return (
    <Container>
      <ImageBackground source={launchscreenBg} style={styles.imageContainer}>
        <H2 style={styles.text}>Welcome to</H2>
        <View style={styles.buttons}>
          <Button
            style={styles.button}
            onPress={() => navigation.navigate('SetUp')}>
            <Text>Start</Text>
          </Button>
        </View>
      </ImageBackground>
    </Container>
  );
}
