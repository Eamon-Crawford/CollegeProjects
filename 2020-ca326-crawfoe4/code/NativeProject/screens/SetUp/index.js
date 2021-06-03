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
  Title,
} from 'native-base';
import {isSensorAvailableTest} from '../../components/Biometrics';

export default function SetUp({navigation}) {
  const [sensorAvailable, setSensorAvailable] = useState(null);
  useEffect(() => {
    async function updateSensorAvailable() {
      console.log('useEffect on SetUp');
      setSensorAvailable(await isSensorAvailableTest());
      console.log('inside useEffect:', sensorAvailable);
    }
    updateSensorAvailable();
  }, [sensorAvailable]);

  function routeToBio() {
    console.log('route:', sensorAvailable);
    if (sensorAvailable) {
      navigation.navigate('SetUpBio');
    } else {
      navigation.navigate('NoBio');
    }
  }

  console.log('sensorAvailable:', sensorAvailable);
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
            <Text>First Time using SafeHouse?</Text>
          </CardItem>
          <CardItem>
            <Body>
              <Text>Click below to get started!</Text>
            </Body>
          </CardItem>
          <CardItem footer>
            <Button onPress={() => routeToBio()}>
              <Text>Begin set up</Text>
            </Button>
          </CardItem>
        </Card>

        <Card>
          <CardItem header>
            <Text>Already have an Account? (Coming Soon)</Text>
          </CardItem>
          <CardItem>
            <Body>
              <Text>Click below to log in and retrieve your set up</Text>
            </Body>
          </CardItem>
          <CardItem footer>
            <Button disabled onPress={() => navigation.navigate('Home')}>
              <Text>Skip set up</Text>
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
