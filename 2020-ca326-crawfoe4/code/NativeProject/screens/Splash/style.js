const React = require('react-native');
const {Dimensions} = React;
const deviceHeight = Dimensions.get('window').height;

export default {
  imageContainer: {
    flex: 1,
  },
  text: {
    marginTop: deviceHeight / 8,
    alignSelf: 'center',
    flex: 5,
    color: 'white',
  },
  buttons: {
    flex: 1,
    flexDirection: 'row',
    color: '#38b4',
    justifyContent: 'flex-end',
  },
  button: {
    flex: 1,
    margin: 5,
  },
};
