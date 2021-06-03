import AsyncStorage from '@react-native-community/async-storage';

export async function setSetUpCompleted() {
  console.log('set up com');
  await AsyncStorage.setItem('HAS_LAUNCHED', 'true');
}

export async function setSetUpNotCompleted() {
  await AsyncStorage.setItem('HAS_LAUNCHED', 'false');
}

export async function checkIfSetUpCompleted() {
  try {
    const setUpCompleted = await AsyncStorage.getItem('HAS_LAUNCHED');
    console.log('setUpCompleted', setUpCompleted);
    if (setUpCompleted === 'true') {
      console.log('setup completed');
      return true;
    } else {
      console.log('setup not done');
      return false;
    }
  } catch (error) {
    console.log(error);
    return false;
  }
}

export async function getUserId() {
  try {
    const userId = await AsyncStorage.getItem('USER_ID');
    console.log('userId IN GET FUNC:', userId);
    if (userId !== null) {
      return userId;
    }
    return null;
  } catch (error) {
    return error;
  }
}

export async function setUserId(value) {
  try {
    console.log('setting user id:', value);
    await AsyncStorage.setItem('USER_ID', value);
    console.log('set user id:', await getUserId());
    return await getUserId();
  } catch (error) {
    console.log('error:', error);
    return error;
  }
}
