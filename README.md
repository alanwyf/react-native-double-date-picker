# react-native-double-date-picker

This is a Android date picker component just simply developed for that start date and end date are required to be in a single modal.

**NOTICE: THIS COMPONENT IS ONLY FOR ANDROID**

# Install
`npm install react-native-double-date-picker --save`

`react-native link`

# Usage
```
import React, {Component} from "react";
import {
  AppRegistry,
  StyleSheet,
  Text,
  View,
} from "react-native";
import DoubleDatePicker from 'DoubleDatePicker';

class DoubleDatePickerExample extends Component {

  componentWillMount() {
    let options = {
      startDate: '2017-06-03',    // optional, default: today, format: 'YYYY-MM-DD'
      endDate: '2017-06-03',      // optional, default: today, format: 'YYYY-MM-DD'
      maxDate: '2017-06-03',      // optional, default: unlimited, format: 'YYYY-MM-DD'
      language: 'Chinese',        // optional, default: Simplified Chinese, options: 'English'/'Chinese'
    };
    this._showPicker(options);
  }

  _showPicker = async (options) => {
    try {
      const {startDate, endDate} = await DoubleDatePicker.show(options);
      // success
      console.log(`startDate: ${startDate}, endDate: ${endDate}`);
    } catch ({code, message}) {
      // error
      console.log(`code: ${code}, message: ${message}`);
    }
  };

  render() {
    return (
      <View style={styles.container}>
        <Text style={styles.welcome}>
          Welcome to DoubleDatePicker!
        </Text>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
  welcome: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10,
  },
});

AppRegistry.registerComponent('DoubleDatePickerExample', () => DoubleDatePickerExample);
```

# Options
option | required | description |
--- | --- | --- | 
startDate | optional |default: today, format: 'YYYY-MM-DD'
endDate | optional |default: today, format: 'YYYY-MM-DD'
maxDate | optional |default: unlimited, format: 'YYYY-MM-DD'
language | optional | default: Simplified Chinese, options: 'English'/'Chinese'
