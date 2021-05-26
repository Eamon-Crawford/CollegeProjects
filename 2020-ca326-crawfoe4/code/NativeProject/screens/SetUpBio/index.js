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
  Title,
  Spinner,
} from 'native-base';
import {Alert} from 'react-native';
import {createKeysAndSendTest} from '../../components/Biometrics';
import {setUserId} from '../../components/AsyncStorage';

function validateEmail(email) {
  var re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
  return re.test(String(email).toLowerCase());
}

export default function SetUpBio({navigation}) {
  const [wait, setWait] = useState(false);
  const [emailValid, setEmailValid] = useState(false);
  const [email, setEmail] = useState('');

  const changeEmail = value => {
    value = value.trim();
    if (validateEmail(value)) {
      setEmailValid(true);
    } else {
      setEmailValid(false);
    }
    setEmail(value);
  };

  async function registerNewUser() {
    setWait(true);
    const userId = await createKeysAndSendTest(email);
    console.log(
      'YOU NEED TO CHECK THE RESPONSE HERE AND FIX THE IF: registerResponse:',
      userId,
    );
    if (userId.split(':')[0] !== 'Error') {
      console.log('saved id:', userId);
      setUserId(userId);
      navigation.navigate('SetUpLock');
    } else {
      Alert.alert('Error registering User, please try again');
    }
    setWait(false);
  }

  return (
    <Container>
      <Header>
        <Body>
          <Title>Set Up</Title>
        </Body>
      </Header>
      <Content>
        <Card>
          <CardItem header>
            <Text>Setting up SafeHouse</Text>
          </CardItem>
          <CardItem>
            <Body>
              <Text>Looks like your device supports fingerprint scanning!</Text>
              <Text>
                You use your devices finger print authentication with SafeHouse
              </Text>
              <Text />
              <Text>Enter your email address below</Text>
            </Body>
          </CardItem>
          <Form>
            {!emailValid && email.length > 0 ? (
              <Item floatingLabel error>
                <Label>Email Address</Label>
                <Input onChangeText={text => changeEmail(text)} value={email} />
              </Item>
            ) : (
              <Item floatingLabel>
                <Label>Email Address</Label>
                <Input onChangeText={text => changeEmail(text)} value={email} />
              </Item>
            )}
          </Form>

          <CardItem footer>
            {emailValid ? (
              <>
                {wait ? (
                  <>
                    <Button disabled>
                      <Text>Waiting</Text>
                      <Spinner />
                    </Button>
                  </>
                ) : (
                  <Button onPress={() => registerNewUser()}>
                    <Text>Submit</Text>
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
}
