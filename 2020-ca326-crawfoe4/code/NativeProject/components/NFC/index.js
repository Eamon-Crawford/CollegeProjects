import NfcManager, {NfcTech, Ndef} from 'react-native-nfc-manager';
import {Alert} from 'react-native';

function buildUrlPayload(valueToWrite) {
  return Ndef.encodeMessage([Ndef.uriRecord(valueToWrite)]);
}

export function cleanUp() {
  NfcManager.cancelTechnologyRequest().catch(() => 0);
}

export async function startNFC() {
  NfcManager.start();
}

export async function writeData(id) {
  try {
    console.log('trsgrgdhfc');
    let resp = await NfcManager.requestTechnology(NfcTech.Ndef, {
      alertMessage: 'Ready to write some NFC tags!',
    });
    console.log(resp);
    let ndef = await NfcManager.getNdefMessage();
    console.log(ndef);
    let bytes = buildUrlPayload(id);
    await NfcManager.writeNdefMessage(bytes);
    //await NfcManager.makeReadOnlyAndroid();
    Alert.alert('Success!');
    cleanUp();
    return true;
  } catch (ex) {
    console.warn('ex', ex);
    cleanUp();
    return false;
  }
}

export async function readData() {
  console.log('attemping to read');
  try {
    let resp = await NfcManager.requestTechnology(NfcTech.Ndef, {
      alertMessage: 'Ready to do some custom Mifare cmd!',
    });
    console.log('rt resp:', resp);
    let tag = await NfcManager.getTag();
    console.log('tag data', tag.ndefMessage[0].payload);
    console.log(
      'hackerman:',
      String.fromCharCode.apply(null, tag.ndefMessage[0].payload),
    );
    const read = String.fromCharCode.apply(null, tag.ndefMessage[0].payload);
    console.log('read', read);
    cleanUp();
    console.log('read', read);
    return read;
  } catch (ex) {
    if (ex === 'cancelled') {
      cleanUp();
      return 'cancelled';
    } else {
      console.warn('ex', ex);
      cleanUp();
    }
    return null;
  }
}
