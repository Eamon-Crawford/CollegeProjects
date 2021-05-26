import React from 'react';
import LoadingSplash from './screens/LoadingSplash';
import Splash from './screens/Splash';
import {NavigationContainer} from '@react-navigation/native';
import {createStackNavigator} from '@react-navigation/stack';
import Home from './screens/Home';
import ShareAccess from './screens/ShareAccess';
import Settings from './screens/Settings';
import SetUp from './screens/SetUp';
import SetUpBio from './screens/SetUpBio';
import NoBio from './screens/NoBio';
import SetUpLock from './screens/SetUpLock';
import SetUpNFC from './screens/SetUpNFC';
import SetUpPin from './screens/SetUpPin';
import TestLock from './screens/TestLock';
import {checkIfSetUpCompleted} from './components/AsyncStorage';

const Stack = createStackNavigator();

export default class App extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      isReady: false,
      routePastSetUp: false,
    };
  }
  async componentDidMount() {
    const hasSetUpCompleted = await checkIfSetUpCompleted();
    console.log('hasSetUpCompleted:', hasSetUpCompleted);
    console.log('has set up', hasSetUpCompleted);
    this.setState({
      routePastSetUp: hasSetUpCompleted,
      isReady: true,
    });
    console.log(this.state.routePastSetUp);
  }

  render() {
    const {isReady, routePastSetUp} = this.state;
    if (!isReady) {
      return <LoadingSplash />;
    }

    return (
      <NavigationContainer>
        <Stack.Navigator
          initialRouteName={routePastSetUp ? 'Home' : 'Splash'}
          screenOptions={{
            headerShown: false,
          }}>
          <Stack.Screen name="Home" component={Home} />
          <Stack.Screen name="ShareAccess" component={ShareAccess} />
          <Stack.Screen name="Settings" component={Settings} />
          <Stack.Screen name="Splash" component={Splash} />
          <Stack.Screen name="SetUp" component={SetUp} />
          <Stack.Screen name="SetUpBio" component={SetUpBio} />
          <Stack.Screen name="NoBio" component={NoBio} />
          <Stack.Screen name="SetUpLock" component={SetUpLock} />
          <Stack.Screen name="SetUpNFC" component={SetUpNFC} />
          <Stack.Screen name="SetUpPin" component={SetUpPin} />
          <Stack.Screen name="TestLock" component={TestLock} />
        </Stack.Navigator>
      </NavigationContainer>
    );
  }
}
