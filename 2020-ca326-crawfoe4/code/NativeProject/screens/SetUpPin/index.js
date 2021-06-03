import React, {useState} from 'react';
import {
  Container,
  Header,
  Content,
  Footer,
  Button,
  Text,
  Card,
  CardItem,
  Body,
  Form,
  Label,
  Input,
  Item,
  Icon,
} from 'native-base';
import {Alert} from 'react-native';
import {createKeysAndSendTest} from '../../components/Biometrics';
import {setIsPinSet} from '../../components/AsyncStorage';
import PINCode from '@haskkor/react-native-pincode';

export default function SetUpBio({navigation}) {
  async function pinSet() {
    Alert.alert('hey listen');
    setIsPinSet('true');
  }

  return <PINCode status={'choose'} finishProcess={pinSet()} />;
}
