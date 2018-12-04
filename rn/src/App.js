import React, { Component } from 'react';
import {
  createAppContainer,
  createStackNavigator,
  createSwitchNavigator,
  createBottomTabNavigator,
} from 'react-navigation';
import { Image } from 'react-native';
import ForgotPassword from './ForgotPassword';
import SignIn from './SignIn';
import HomeScreen from './HomeScreen';
import Me from './Me';

export default class App extends Component {
  render() {
    return <AppContainer />;
  }
}

const AuthenticationNavigator = createStackNavigator({
  SignIn,
  ForgotPassword,
});

const BottomTabNavigator = createBottomTabNavigator({
  Home: {
    screen: HomeScreen,
    path: 'Home',
  },
  Me,
}, {
  navigationOptions: ({ navigation }) => {
    const { routeName } = navigation.state;
    // let tabBarVisible = true;
    return {
      // tabBarVisible,
      // tabBarLabel: getTabbarTitle(routeName),
      tabBarIcon: ({ focused }) => {
        let iconName;
        if (routeName === 'Home') {
          iconName = focused ? require('./resource/homeIcon-select.png') : require('./resource/homeIcon.png');
        } else if (routeName === 'Me') {
          iconName = focused ? require('./resource/meIcon-select.png') : require('./resource/meIcon.png');
        }
        return <Image resizeMode="contain" style={{ width: 20, height: 20 }} source={iconName} />;
      },
    };
  },
});

const AppNavigator = createSwitchNavigator({
  Main: BottomTabNavigator,
  Auth: AuthenticationNavigator,
});

const AppContainer = createAppContainer(AppNavigator);
