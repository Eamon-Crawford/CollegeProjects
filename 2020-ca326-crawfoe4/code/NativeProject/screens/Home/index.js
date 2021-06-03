import React, {useState, useEffect, setState} from 'react';
import {
  Container,
  Header,
  Title,
  Content,
  Footer,
  FooterTab,
  Button,
  Left,
  Right,
  Body,
  Icon,
  Text,
  Subtitle,
  Card,
  CardItem,
  Spinner,
} from 'native-base';
import Api from '../../components/Api';
import {createBioSignature} from '../../components/Biometrics';
import {startNFC, readData, cleanUp} from '../../components/NFC';
import {getUserId} from '../../components/AsyncStorage';
import {Alert} from 'react-native';

export default function Home({navigation}) {
  const [wait, setWait] = useState();
  useEffect(() => {
    setWait(true);
    NFC();
    return () => cleanUp();
  }, []);

  async function cancelScan() {
    cleanUp();
    setWait(false);
  }

  async function NFC() {
    setWait(true);
    await startNFC();
    const data = await readData();
    console.log('data:', data);
    if (data != null && data !== 'cancelled') {
      const userId = await getUserId();
      const payload = JSON.stringify({LockId: data, UserId: userId});
      const signature = await createBioSignature(payload);
      const registorResponse = await Api.postOpenLock(signature, payload);
      console.log('registorResponse:', registorResponse);
      if (registorResponse.isError) {
        Alert.alert('Error: you dont have access to this lock');
      } else {
        Alert.alert('Success!');
      }
    }
    setWait(false);
  }

  return (
    <Container>
      <Header>
        <Body>
          <Title>Home</Title>
        </Body>
      </Header>
      <Content>
        <Card>
          <CardItem header>
            <Text>Scan your Lock</Text>
          </CardItem>
          <CardItem>
            <Body>
              <Text>Please scan the NFC tag</Text>
              <Text>When scanned your lock should open</Text>
            </Body>
          </CardItem>
          <CardItem footer>
            {wait ? (
              <>
                <Button disabled>
                  <Text>Scanning</Text>
                  <Spinner />
                </Button>
                <Button onPress={() => cancelScan()}>
                  <Text>Cancel</Text>
                </Button>
              </>
            ) : (
              <Button onPress={() => NFC()}>
                <Text>Scan Lock</Text>
              </Button>
            )}
          </CardItem>
        </Card>
      </Content>
      <Footer>
        <FooterTab>
          <Button active>
            <Text>Home</Text>
          </Button>
          <Button onPress={() => navigation.replace('ShareAccess')}>
            <Text>Share Access</Text>
          </Button>
          <Button onPress={() => navigation.replace('Settings')}>
            <Text>Settings</Text>
          </Button>
        </FooterTab>
      </Footer>
    </Container>
  );
}
