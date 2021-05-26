import React, {useState, useEffect} from 'react';
import {
  Container,
  Header,
  Content,
  Footer,
  FooterTab,
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
import {createBioSignature} from '../../components/Biometrics';
import {startNFC, readData, cleanUp} from '../../components/NFC';
import {getUserId} from '../../components/AsyncStorage';
import {Alert} from 'react-native';

function validateEmail(email) {
  var re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
  return re.test(String(email).toLowerCase());
}

export default function ShareAccess({navigation}) {
  const [wait, setWait] = useState(false);
  const [userEmail, setUserEmail] = useState('');
  const [userEmailValid, setUserEmailValid] = useState(false);

  useEffect(() => {
    return () => {
      cleanUp();
    };
  }, []);

  const changeUserEmail = value => {
    value = value.trim();
    if (validateEmail(value)) {
      setUserEmailValid(true);
    } else {
      setUserEmailValid(false);
    }
    setUserEmail(value);
  };

  async function cancel() {
    cleanUp();
    setWait(false);
  }

  async function addUser() {
    setWait(true);
    await startNFC();
    const data = await readData();
    if (data != null && data !== 'cancelled') {
      const userId = await getUserId();
      const payload = JSON.stringify({
        LockId: data,
        UserId: userId,
        Email: userEmail,
      });
      console.log('payload', payload);
      const signature = await createBioSignature(payload);
      const registorResponse = await Api.postAdminAddUser(signature, payload);
      console.log(registorResponse);
      if (registorResponse.isError) {
        Alert.alert('Error: you dont have access to this lock');
      } else {
        Alert.alert('Success!');
      }
    }
    setWait(false);
  }

  return (
    <>
      <Container>
        <Header>
          <Body>
            <Title>Share Access</Title>
          </Body>
        </Header>
        <Content>
          <Card>
            <CardItem header>
              <Text>Add a User</Text>
            </CardItem>
            <CardItem>
              <Body>
                <Text>Please scan the NFC tag for your lock</Text>
              </Body>
            </CardItem>
            <Form>
              {!userEmailValid && userEmail.length > 0 ? (
                <Item floatingLabel error>
                  <Label>Email Address</Label>
                  <Input
                    onChangeText={text => changeUserEmail(text)}
                    value={userEmail}
                  />
                </Item>
              ) : (
                <Item floatingLabel>
                  <Label>Email Address</Label>
                  <Input
                    onChangeText={text => changeUserEmail(text)}
                    value={userEmail}
                  />
                </Item>
              )}
            </Form>
            <CardItem footer>
              {wait ? (
                <>
                  <Button disabled>
                    <Text>Scanning</Text>
                    <Spinner />
                  </Button>
                  <Button onPress={() => cancel()}>
                    <Text>Cancel</Text>
                  </Button>
                </>
              ) : (
                <Button onPress={() => addUser()}>
                  <Text>Scan</Text>
                </Button>
              )}
            </CardItem>
          </Card>
        </Content>
        <Footer>
          <FooterTab>
            <Button onPress={() => navigation.replace('Home')}>
              <Text>Home</Text>
            </Button>
            <Button active>
              <Text>Share Access</Text>
            </Button>
            <Button onPress={() => navigation.replace('Settings')}>
              <Text>Settings</Text>
            </Button>
          </FooterTab>
        </Footer>
      </Container>
    </>
  );
}
