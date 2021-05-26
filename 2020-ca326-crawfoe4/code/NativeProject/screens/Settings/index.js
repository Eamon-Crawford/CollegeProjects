import React from 'react';
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
} from 'native-base';
import {Alert} from 'react-native';
import {setSetUpNotCompleted} from '../../components/AsyncStorage';

export default function Page3({navigation}) {
  return (
    <Container>
      <Header>
        <Body>
          <Title>Settings</Title>
        </Body>
      </Header>
      <Content>
        <Card>
          <Card>
            <CardItem header>
              <Text>Add a new lock</Text>
            </CardItem>
            <CardItem>
              <Body>
                <Text>If you wish to add an additional lock click below</Text>
              </Body>
            </CardItem>
            <CardItem />
            <CardItem footer>
              <Button onPress={() => navigation.navigate('SetUpLock')}>
                <Text>Add Lock</Text>
              </Button>
            </CardItem>
          </Card>
          <CardItem header>
            <Text>Delete Account</Text>
          </CardItem>
          <CardItem>
            <Body>
              <Text>Wipe data</Text>
              <Text>
                Wipe all data for SafeHouse, after doing so you wil have to make
                a new account
              </Text>
            </Body>
          </CardItem>
          <CardItem />
          <CardItem footer>
            <Button onPress={() => setSetUpNotCompleted()}>
              <Text>Wipe</Text>
            </Button>
          </CardItem>
        </Card>
      </Content>
      <Footer>
        <FooterTab>
          <Button onPress={() => navigation.replace('Home')}>
            <Text>Home</Text>
          </Button>
          <Button onPress={() => navigation.replace('ShareAccess')}>
            <Text>ShareAccess</Text>
          </Button>
          <Button active>
            <Text>Settings</Text>
          </Button>
        </FooterTab>
      </Footer>
    </Container>
  );
}
