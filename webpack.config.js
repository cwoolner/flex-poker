const webpack = require('webpack');
const path = require('path');

module.exports = {
  entry: {
    app: './src/main/webapp/resources/index.js'
  },

  output: {
    path: path.join(__dirname, "src/main/webapp/resources"),
    filename: '[name].bundle.js'
  },

  optimization: {
    splitChunks: {
      cacheGroups: {
        vendor: {
          test: /node_modules/,
          chunks: 'initial',
          name: 'vendor',
          priority: 10,
          enforce: true
        }
      }
    }
  },

  module: {
    rules: [
      {
        test: /\.js$/,
        exclude: /node_modules/,
        use: [{
          loader: 'babel-loader',
          options: {
            presets: [
              '@babel/react',
              [
                '@babel/preset-env',
                {
                  "targets": {
                    "esmodules": true
                  },
                  "modules": false,
                  "useBuiltIns": "entry"
                }
              ]
            ]
          }
        }]
      }
    ]
  }
}
