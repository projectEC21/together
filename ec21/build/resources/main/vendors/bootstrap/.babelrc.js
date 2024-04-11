<<<<<<< HEAD
module.exports = {
  presets: [
    [
      '@babel/env',
      {
        loose: true,
        modules: false,
        exclude: ['transform-typeof-symbol']
      }
    ]
  ],
  plugins: [
    '@babel/plugin-proposal-object-rest-spread'
  ],
  env: {
    test: {
      plugins: [ 'istanbul' ]
    }
  }
};
=======
module.exports = {
  presets: [
    [
      '@babel/env',
      {
        loose: true,
        modules: false,
        exclude: ['transform-typeof-symbol']
      }
    ]
  ],
  plugins: [
    '@babel/plugin-proposal-object-rest-spread'
  ],
  env: {
    test: {
      plugins: [ 'istanbul' ]
    }
  }
};
>>>>>>> 1a1ecd553fc320897f03b38db9609eb13cd29bc3
