import React, {Component} from 'react';
import {ImageBackground, View, StatusBar} from 'react-native';
import {Container} from 'native-base';

import styles from './style';

const launchscreenBg = require('../../assets/splashScreen.png');

class LoadingSplash extends Component {
  static navigationOptions = {
    header: null,
  };
  render() {
    return (
      <Container>
        <ImageBackground
          source={launchscreenBg}
          style={styles.imageContainer}
        />
      </Container>
    );
  }
}

export default LoadingSplash;
