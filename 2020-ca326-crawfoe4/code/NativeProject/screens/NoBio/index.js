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
} from 'native-base';
import {isSensorAvailableTest} from '../../components/Biometrics';

export default function NoBio({navigation}) {
  const [sensorAvailable, setSensorAvailable] = useState(null);
  useEffect(() => {
    async function updateSensorAvailable() {
      console.log('useEffect on SetUp');
      setSensorAvailable(await isSensorAvailableTest());
      console.log('inside useEffect:', sensorAvailable);
    }
    updateSensorAvailable();
  }, [sensorAvailable]);

  return (
    <Container>
      <Header />
      <Content>
        <Card>
          <CardItem header>
            <Text>Setting up SafeHouse</Text>
          </CardItem>
          <CardItem>
            {sensorAvailable ? (
              <Body>
                <Text>
                  Looks like your device supports fingerprint scanning!
                </Text>
              </Body>
            ) : (
              <Body>
                <Text>
                  Looks like your device doesnt supports fingerprint scanning!
                </Text>
                <Text>
                  If you havent set up the finger print biometrics on your
                  device yet, please go to your devices settings and turn it on.
                  Otherwise you can continue with a pin.
                </Text>
              </Body>
            )}
          </CardItem>
          <CardItem footer>
            {console.log('sensorAvailble', sensorAvailable)}
            {sensorAvailable ? (
              <Button onPress={() => navigation.navigate('SetUpBio')}>
                <Text>Use a finger print</Text>
              </Button>
            ) : (
              <Button disabled>
                <Text>Use a finger print</Text>
              </Button>
            )}
            <Button disabled onPress={() => navigation.navigate('SetUpPin')}>
              <Text>(WIP)Use a Pin</Text>
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
