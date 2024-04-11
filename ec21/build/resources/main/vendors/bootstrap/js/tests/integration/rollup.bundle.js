<<<<<<< HEAD
/* eslint-env node */

const resolve = require('rollup-plugin-node-resolve')
const commonjs = require('rollup-plugin-commonjs')
const babel = require('rollup-plugin-babel')

module.exports = {
  input: 'js/tests/integration/bundle.js',
  output: {
    file: 'js/coverage/bundle.js',
    format: 'iife'
  },
  plugins: [
    resolve(),
    commonjs(),
    babel({
      exclude: 'node_modules/**'
    })
  ]
}
=======
/* eslint-env node */

const resolve = require('rollup-plugin-node-resolve')
const commonjs = require('rollup-plugin-commonjs')
const babel = require('rollup-plugin-babel')

module.exports = {
  input: 'js/tests/integration/bundle.js',
  output: {
    file: 'js/coverage/bundle.js',
    format: 'iife'
  },
  plugins: [
    resolve(),
    commonjs(),
    babel({
      exclude: 'node_modules/**'
    })
  ]
}
>>>>>>> 1a1ecd553fc320897f03b38db9609eb13cd29bc3
