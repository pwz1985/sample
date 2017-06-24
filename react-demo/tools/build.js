// var config = requi'../webpack.config';
var webpack = require('webpack');
var config = require('../webpack.config')

new webpack(config, function() {
  console.log('编译完成');
  console.log(arguments);
});
