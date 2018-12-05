import React, { Component } from 'react';
import { StyleSheet, Text, View } from 'react-native';

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
  },
});

export default class Me extends Component {
  render() {
    return (
      <View style={styles.container}>
        <Text>Me</Text>
      </View>
    );
  }
}
