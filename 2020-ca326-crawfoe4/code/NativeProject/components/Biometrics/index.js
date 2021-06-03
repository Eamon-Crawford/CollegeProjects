import ReactNativeBiometrics from 'react-native-biometrics';
import Api from '../Api';
import {Alert} from 'react-native';

export async function isSensorAvailableTest() {
  try {
    const resultObject = await ReactNativeBiometrics.isSensorAvailable();
    const resolved = resultObject;
    console.log('resultObject:', resultObject);
    const {available, biometryType} = resolved;
    if (available && biometryType === ReactNativeBiometrics.TouchID) {
      console.log('TouchID is supported');
    } else if (available && biometryType === ReactNativeBiometrics.FaceID) {
      console.log('FaceID is supported');
    } else if (available && biometryType === ReactNativeBiometrics.Biometrics) {
      console.log('Biometrics is supported');
    } else {
      console.log('Biometrics not supported');
    }
    console.log('resolved.available:', resolved.available);
    return resolved.available;
  } catch (e) {
    console.log(e);
  }
}

// export async function isSensorAvailableTest() {
//   const result = await ReactNativeBiometrics.isSensorAvailable().then(
//     resultObject => {
//       const {available, biometryType} = resultObject;
//       console.log(resultObject);
//       if (available && biometryType === ReactNativeBiometrics.TouchID) {
//         console.log('TouchID is supported');
//       } else if (available && biometryType === ReactNativeBiometrics.FaceID) {
//         console.log('FaceID is supported');
//       } else if (
//         available &&
//         biometryType === ReactNativeBiometrics.Biometrics
//       ) {
//         console.log('Biometrics is supported');
//       } else {
//         console.log('Biometrics not supported');
//       }
//       return resultObject;
//     },
//   );
//   console.log('result:', result);
//   if (result.available) {
//     console.log('yes', result);
//     return result.available;
//   } else {
//     console.log('nothing');
//     return false;
//   }
// }

export async function createKeysAndSendTest(email) {
  const result = await ReactNativeBiometrics.createKeys(
    'Confirm fingerprint',
  ).then(async resultObject => {
    console.log(resultObject);
    const {publicKey} = await resultObject;
    console.log(publicKey);
    return publicKey;
  });
  console.log('create keys result:', result);
  const response = await sendPublicKey(result, email);
  console.log('response:', response);
  return response;
}

export async function sendPublicKey(publicKey, email) {
  const saved = await Api.postPublicKey(publicKey, email);
  console.log(saved);
  return saved;
}

export function keysExistTest() {
  ReactNativeBiometrics.biometricKeysExist().then(resultObject => {
    const {keysExist} = resultObject;

    if (keysExist) {
      console.log('Keys exist');
      return true;
    } else {
      console.log('Keys do not exist or were deleted');
      return false;
    }
  });
}

export function delKeysTest() {
  ReactNativeBiometrics.deleteKeys().then(resultObject => {
    const {keysDeleted} = resultObject;

    if (keysDeleted) {
      console.log('Successful deletion');
    } else {
      console.log('Unsuccessful deletion because there were no keys to delete');
    }
  });
}

export async function createBioSignature(payload) {
  console.log('creating signature');
  const result = await ReactNativeBiometrics.createSignature({
    promptMessage: 'Sign in',
    payload: payload,
  });
  console.log('resultObject:', result);
  return result.signature;
}

export async function createBioSignatureTest() {
  let epochTimeSeconds = Math.round(new Date().getTime() / 1000).toString();
  let payload = epochTimeSeconds + 'testing creating a signature';
  console.log(payload);
  const result = await ReactNativeBiometrics.createSignature({
    promptMessage: 'Sign in',
    payload: payload,
  }).then(async resultObject => {
    console.log('ResObj', resultObject);
    console.log('payload', payload);
    const {success, signature} = resultObject;
    // if (success) {
    //   const response = await verifySignatureWithServer(signature, payload);
    //   if (response) {
    //     return signature;
    //   }
    // }
    // return null;
    return signature;
  });
  console.log('createBioSignature signature:', result);
  if (result !== null && result !== undefined) {
    return result;
  } else if (result === undefined) {
    return null;
  } else {
    console.error('error verifying signature');
  }
}

// export function createPinSignatureTest() {
//   let epochTimeSeconds = Math.round(new Date().getTime() / 1000).toString();
//   let payload = epochTimeSeconds + 'some message';

//   ReactNativeBiometrics.createSignature({
//     promptMessage: 'Sign in',
//     payload: payload,
//   }).then(resultObject => {
//     const {success, signature} = resultObject;

//     if (success) {
//       console.log(signature);
//       verifySignatureWithServer(signature, payload);
//     }
//   });
// }

export async function verifySignatureWithServer(signature, payload) {
  console.log('Signature:', signature);
  console.log('payload:', payload);
  const saved = await Api.postVerifySignature(signature, payload);
  if (saved.isError) {
    console.log(saved);
  } else {
    console.log(saved);
  }
  setTimeout(function() {
    console.log('timeout');
  }, 2000);
}
