var path = require('path')
var HtmlWebpackPlugin = require('html-webpack-plugin');

var config = {
  entry: "./src/index.tsx",
  output: {
    filename: "bundle.js",
    path: __dirname + "/dist"
  },
  resolve: {
    modules: [path.resolve(__dirname, "src"), "node_modules"],
    extensions: [".web.js", ".js", ".ts", ".tsx", ".json"]
  },
  module: {
    rules: [{
        test: /\.tsx?$/,
        use: "awesome-typescript-loader"
      },
      {
        test: /\.less$/,
        use: ['style-loader', 'css-loader', 'less-loader']
      },
    ]
  },
  devServer: {
    contentBase: path.join(__dirname, "dist"),
    compress: true,
    port: 3000
  },
  plugins: [new HtmlWebpackPlugin({
    filename: 'index.html',
    template: 'src/index.html'
  })]
}

module.exports = config
