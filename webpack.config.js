module.exports = {
  entry: './src/main/webapp/resources/index.js',

  output: {
    filename: 'src/main/webapp/resources/bundle.js',
    publicPath: ''
  },

  module: {
    loaders: [
      { test: /\.js$/, exclude: /node_modules/, loader: 'babel-loader?presets[]=es2015&presets[]=react' }
    ]
  }
}
