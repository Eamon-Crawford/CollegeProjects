import React, {useState, useEffect} from 'react';
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
import NfcManager, {NfcTech, Ndef} from 'react-native-nfc-manager';
import {Alert} from 'react-native';

function buildUrlPayload(valueToWrite) {
  return Ndef.encodeMessage([Ndef.uriRecord(valueToWrite)]);
}

export default function SetUpNFC({navigation}) {
  const [text, setText] = useState('');

  const changeTextValue = value => {
    console.log(value);
    setText(value);
  };

  useEffect(() => {
    console.log('starting listener');
    NfcManager.start();
    return () => {
      cleanUp();
    };
  }, []);

  const cleanUp = () => {
    NfcManager.cancelTechnologyRequest().catch(() => 0);
  };

  const writeData = async () => {
    try {
      console.log('trsgrgdhfc');
      let resp = await NfcManager.requestTechnology(NfcTech.Ndef, {
        alertMessage: 'Ready to write some NFC tags!',
      });
      console.warn(resp);
      let ndef = await NfcManager.getNdefMessage();
      console.warn(ndef);
      let bytes = buildUrlPayload('lara');
      await NfcManager.writeNdefMessage(bytes);
      console.warn('successfully write ndef');
      await NfcManager.makeReadOnlyAndroid();
      Alert.alert('I got your tag!');
      cleanUp();
    } catch (ex) {
      console.warn('ex', ex);
      cleanUp();
    }
  };

  const readData = async () => {
    console.log('attemping to read');
    try {
      let resp = await NfcManager.requestTechnology(NfcTech.Ndef, {
        alertMessage: 'Ready to do some custom Mifare cmd!',
      });
      console.warn('rt resp:', resp);
      let tag = await NfcManager.getTag();
      console.warn('tag data', tag.ndefMessage[0].payload);
      console.log(
        'hackerman:',
        String.fromCharCode.apply(null, tag.ndefMessage[0].payload),
      );
      cleanUp();
    } catch (ex) {
      console.warn('ex', ex);
      cleanUp();
    }
  };

  return (
    <Container>
      <Header />
      <Content>
        <Card>
          <CardItem header>
            <Text>Add your Lock</Text>
          </CardItem>
          <CardItem>
            <Body>
              <Text>
                On the side of your SafeHouse lock you should find an key called
                Lock ID.
              </Text>
              <Text>Enter this Key below</Text>
            </Body>
          </CardItem>
          <Form>
            <Item floatingLabel>
              <Label>Lock ID</Label>
              <Input
                onChangeText={input => changeTextValue(input)}
                value={text}
              />
            </Item>
          </Form>
          <CardItem footer>
            <Button onPress={() => readData()}>
              <Text>readData</Text>
            </Button>
            <Button onPress={() => writeData()}>
              <Text>writeData</Text>
            </Button>
          </CardItem>
          <CardItem footer>
            <Button onPress={() => cleanUp()}>
              <Text>cancel</Text>
            </Button>
          </CardItem>
        </Card>
      </Content>
      <Footer>
        <Text> SafeHouse by Eamon Crawford and Sean Hammond</Text>
      </Footer>
    </Container>
  );
}
