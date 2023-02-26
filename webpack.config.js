const webpack = require('webpack');
const path = require('path');

module.exports = {
  entry: {
    app: './src/main/webapp/resources/index.tsx',
  },

  output: {
    path: path.join(__dirname, 'src/main/webapp/resources'),
    filename: '[name].bundle.js',
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

  resolve: {
    extensions: ['.tsx', '.ts', '.js'],
  },

  module: {
    rules: [
      {
        test: /\.tsx?$/,
        use: [
          {
            loader: 'ts-loader',
            options: {
    //          transpileOnly: true,
            }
          }
        ],
        exclude: [
          '/node_modules/',
          '/documentation/',
          '/bin/',
          '/target/',
          '/output/',
          '/dist/',
        ]
      }
    ]
  }
}
