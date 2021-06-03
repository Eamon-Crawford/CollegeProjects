import React, {useState, setState} from 'react';
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
} from 'native-base';
import Api from '../../components/Api';
import {createBioSignatureTest} from '../../components/Biometrics';
import {Alert} from 'react-native';
import QRCodeScanner from 'react-native-qrcode-scanner';

export default function SetUp() {
  const [tested, setTested] = useState(false);
  const [lockId, setLockId] = useState('');

  function testLock() {
    console.log('scan');
    setTested(true);
    Api.postTest();
  }

  function finish() {
    console.log('finish');
  }

  return (
    <>
      <Container>
        <Header />
        <Content>
          <Card>
            <CardItem header>
              <Text>Lets test your lock</Text>
            </CardItem>
            <CardItem>
              <Body>
                <Text>
                  When you press the button below, your lock should close and
                  open once
                </Text>
              </Body>
            </CardItem>
            <CardItem />
            <CardItem footer>
              <Button onPress={() => testLock()}>
                <Text>Test Lock</Text>
              </Button>
            </CardItem>
          </Card>
          {tested && (
            <Card>
              <CardItem header>
                <Text>Did it work?</Text>
              </CardItem>
              <CardItem>
                <Body>
                  <Text>
                    Your press cfinish below to complete your lock set up
                  </Text>
                  <Text>
                    This will lock your SafeHouse Lock, so please ensure the
                    test is working before hitting finish
                  </Text>
                </Body>
              </CardItem>
              <CardItem />
              <CardItem footer>
                <Button onPress={() => finish()}>
                  <Text>Finish</Text>
                </Button>
              </CardItem>
            </Card>
          )}
        </Content>
        <Footer>
          <Text>SafeHouse by Eamon Crawford and Sean Hammond</Text>
        </Footer>
      </Container>
    </>
  );
}
