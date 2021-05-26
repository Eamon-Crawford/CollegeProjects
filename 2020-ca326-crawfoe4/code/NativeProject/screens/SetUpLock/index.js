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
  Spinner,
  Title,
} from 'native-base';
import Api from '../../components/Api';
import {startNFC, writeData, readData} from '../../components/NFC';
import {createBioSignature} from '../../components/Biometrics';
import {Alert} from 'react-native';
import QRCodeScanner from 'react-native-qrcode-scanner';
import {getUserId, setSetUpCompleted} from '../../components/AsyncStorage';

export default function SetUp({navigation}) {
  const [waitWrite, setWaitWrite] = useState(false);
  const [waitRead, setWaitRead] = useState(false);
  const [screen, setScreen] = useState('preScan');
  const [lockId, setLockId] = useState('');
  const [lockName, setLockName] = useState('');
  const [lockNameValid, setLockNameValid] = useState(false);

  function changeLockName(text) {
    if (/^[A-Za-z0-9 ]{0,24}$/i.test(text)) {
      setLockName(text);
      if (text.length < 24 && text.length > 1) {
        setLockNameValid(true);
      } else {
        setLockNameValid(false);
      }
    }
  }

  async function testNewLockId(e) {
    setScreen('afterScan');
    console.log('setting lock id:', e.data);
    setLockId(e.data);
    const userId = await getUserId();
    const payload = JSON.stringify({LockId: e.data, UserId: userId});
    const signature = await createBioSignature(payload);
    console.log('SENDING:', 'signature:', signature, 'payload:', payload);
    const registerResponse = await Api.postTestLock(signature, payload);
    console.log(registerResponse);
    if (registerResponse === null) {
      Alert.alert('Error');
    }
  }

  function scanQRCode() {
    console.log('scan');
    setScreen('scan');
  }

  async function retest() {
    const userId = await getUserId();
    console.log('did we set lockid?:', lockId);
    const payload = JSON.stringify({LockId: lockId, UserId: userId});
    const signature = await createBioSignature(payload);
    const registorResponse = await Api.postTestLock(signature, payload);
    console.log(registorResponse);
  }

  function promptInitLock() {
    Alert.alert('Are you sure?', 'Lock will be locked!', [
      {
        text: 'No',
        onPress: () => console.log('Cancel Pressed'),
        style: 'cancel',
      },
      {text: 'Yes', onPress: () => initLock()},
    ]);
  }

  async function initLock() {
    const userId = await getUserId();
    const payload = JSON.stringify({
      LockId: lockId,
      UserId: userId,
      LockName: lockName,
    });
    const signature = await createBioSignature(payload);
    console.log(signature);
    if (typeof signature !== 'undefined') {
      const response = Api.postInitLock(signature, payload);
      if (response !== null) {
        await setSetUpCompleted();
        navigation.reset({
          index: 0,
          routes: [
            {
              name: 'Home',
            },
          ],
        });
        console.log('finish');
      }
    }
    console.log('canceled');
  }

  function skipAddLock() {
    setSetUpCompleted();
    navigation.reset({
      index: 0,
      routes: [
        {
          name: 'Home',
        },
      ],
    });
  }

  async function beginWrite() {
    setWaitWrite(true);
    await startNFC();
    const writeSuccess = await writeData(lockId);
    if (writeSuccess) {
      setScreen('read');
    } else {
      setScreen('setName');
    }
    setWaitWrite(false);
  }

  async function beginRead() {
    setWaitRead(true);
    await startNFC();
    const readSuccess = await readData();
    if (readSuccess !== null) {
      setScreen('final');
    } else {
      setScreen('read');
    }
    setWaitRead(false);
  }

  switch (screen) {
    case 'scan':
      return (
        <>
          <Text>Please Scan the QR Code that came with your lock</Text>
          <QRCodeScanner onRead={e => testNewLockId(e)} />
        </>
      );
    case 'preScan':
      return (
        <Container>
          <Header>
            <Body>
              <Title>Scan</Title>
            </Body>
          </Header>
          <Content>
            <Card>
              <CardItem header>
                <Text>Add your Lock</Text>
              </CardItem>
              <CardItem>
                <Body>
                  <Text>Please scan the QR code that came with your lock</Text>
                  <Text>
                    After scanning your lock should close and open again
                  </Text>
                  <Text />
                </Body>
              </CardItem>
              <CardItem />
              <CardItem footer>
                <Button onPress={() => scanQRCode()}>
                  <Text>Scan</Text>
                </Button>
              </CardItem>
            </Card>
            <Card>
              <CardItem header>
                <Text>
                  If you dont wait to add a lock at this moment, press skip
                  below
                </Text>
              </CardItem>
              <CardItem />
              <CardItem footer>
                <Button onPress={() => skipAddLock()}>
                  <Text>Skip</Text>
                </Button>
              </CardItem>
            </Card>
          </Content>
          <Footer>
            <Text>SafeHouse by Eamon Crawford and Sean Hammond</Text>
          </Footer>
        </Container>
      );
    case 'afterScan':
      return (
        <Container>
          <Header>
            <Body>
              <Title>Test Lock</Title>
            </Body>
          </Header>
          <Content>
            <Card>
              <CardItem header>
                <Text>Test again?</Text>
              </CardItem>
              <CardItem>
                <Body>
                  <Text>Your Lock should've just closed and opened</Text>
                  <Text>
                    If your lock didnt show activity, press the re-test button
                    below
                  </Text>
                  <Text />
                  <Text>Press Continue When you are happy to move on</Text>
                </Body>
              </CardItem>
              <CardItem footer>
                <Button onPress={() => scanQRCode()}>
                  <Text>Scan</Text>
                </Button>
                <Button onPress={() => retest()}>
                  <Text>Re-Test</Text>
                </Button>
                <Button onPress={() => setScreen('setName')}>
                  <Text>Continue</Text>
                </Button>
              </CardItem>
            </Card>
          </Content>
          <Footer>
            <Text>SafeHouse by Eamon Crawford and Sean Hammond</Text>
          </Footer>
        </Container>
      );
    case 'setName':
      return (
        <Container>
          <Header>
            <Body>
              <Title>Name Lock</Title>
            </Body>
          </Header>
          <Content>
            <Card>
              <CardItem header>
                <Body>
                  <Text>Set a Name for your lock</Text>
                </Body>
              </CardItem>
              <Form>
                {lockNameValid ? (
                  <Item floatingLabel>
                    <Label>Lock Name</Label>
                    <Input
                      onChangeText={text => changeLockName(text)}
                      value={lockName}
                    />
                  </Item>
                ) : (
                  <Item floatingLabel error>
                    <Label>Lock Name</Label>
                    <Input
                      onChangeText={text => changeLockName(text)}
                      value={lockName}
                    />
                  </Item>
                )}
              </Form>
              <Text />
            </Card>
            <Card>
              <CardItem header>
                <Text>Write to NFC card</Text>
              </CardItem>
              <CardItem>
                <Body>
                  <Text>
                    Press 'Write' below and bring your NFC card into contact
                    with your device
                  </Text>
                </Body>
              </CardItem>
              <CardItem footer>
                {lockNameValid ? (
                  <>
                    {waitWrite ? (
                      <Button disabled>
                        <Text>Writing</Text>
                        <Spinner />
                      </Button>
                    ) : (
                      <Button onPress={() => beginWrite()}>
                        <Text>Write</Text>
                      </Button>
                    )}
                  </>
                ) : (
                  <Button disabled>
                    <Text>Submit</Text>
                  </Button>
                )}
              </CardItem>
            </Card>
          </Content>
          <Footer>
            <Text>SafeHouse by Eamon Crawford and Sean Hammond</Text>
          </Footer>
        </Container>
      );
    case 'read':
      return (
        <Container>
          <Header>
            <Body>
              <Title>Test NFC</Title>
            </Body>
          </Header>
          <Content>
            <Card>
              <CardItem header>
                <Text>Test NFC card</Text>
              </CardItem>
              <CardItem>
                <Body>
                  <Text>
                    Press 'Read' below and bring your device into contact with
                    your NFC card
                  </Text>
                </Body>
              </CardItem>
              <CardItem footer>
                {waitRead ? (
                  <Button disabled>
                    <Text>Reading</Text>
                    <Spinner />
                  </Button>
                ) : (
                  <Button onPress={() => beginRead()}>
                    <Text>Read</Text>
                  </Button>
                )}
              </CardItem>
            </Card>
          </Content>
          <Footer>
            <Text>SafeHouse by Eamon Crawford and Sean Hammond</Text>
          </Footer>
        </Container>
      );
    case 'final':
      return (
        <Container>
          <Header>
            <Body>
              <Title>Finish</Title>
            </Body>
          </Header>
          <Content>
            <Card>
              <CardItem header>
                <Text>Done</Text>
              </CardItem>
              <CardItem>
                <Body>
                  <Text>
                    If your happy that your lock is functioning correctly,
                    please press finish below
                  </Text>
                  <Text />
                  <Text>
                    This will activate your lock, locking it for real!
                  </Text>
                </Body>
              </CardItem>
              <CardItem footer>
                <Button onPress={() => promptInitLock()}>
                  <Text>Finish</Text>
                </Button>
              </CardItem>
            </Card>
          </Content>
          <Footer>
            <Text>SafeHouse by Eamon Crawford and Sean Hammond</Text>
          </Footer>
        </Container>
      );
    default:
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
                  <Text>Please scan the QR code that came with your lock</Text>
                  <Text>
                    After scanning your lock should close and open again
                  </Text>
                  <Text />
                </Body>
              </CardItem>
              <CardItem />
              <CardItem footer>
                <Button onPress={() => scanQRCode()}>
                  <Text>Scan</Text>
                </Button>
              </CardItem>
            </Card>
            <Card>
              <CardItem header>
                <Text>
                  If you dont wait to add a lock at this moment, press skip
                  below
                </Text>
              </CardItem>
              <CardItem />
              <CardItem footer>
                <Button onPress={() => skipAddLock()}>
                  <Text>Skip</Text>
                </Button>
              </CardItem>
            </Card>
          </Content>
          <Footer>
            <Text>SafeHouse by Eamon Crawford and Sean Hammond</Text>
          </Footer>
        </Container>
      );
  }
}
