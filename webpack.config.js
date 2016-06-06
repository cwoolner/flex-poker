const webpack = require('webpack');

module.exports = {
  entry: {
    app: './src/main/webapp/resources/index.js',
    vendor: [
      'lodash',
      'react',
      'react-bootstrap',
      'react-dom',
      'react-router'
    ]
  },

  output: {
    filename: 'src/main/webapp/resources/bundle.js',
    publicPath: ''
  },

  plugins: [
    new webpack.optimize.CommonsChunkPlugin('vendor', 'src/main/webapp/resources/vendor.bundle.js')
  ],

  module: {
    loaders: [
      { test: /\.js$/, exclude: /node_modules/, loader: 'babel-loader?presets[]=es2015&presets[]=react' }
    ]
  }
}
