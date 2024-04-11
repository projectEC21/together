<<<<<<< HEAD
'use strict'

module.exports = (ctx) => ({
  map: ctx.file.dirname.includes('examples') ? false : {
    inline: false,
    annotation: true,
    sourcesContent: true
  },
  plugins: {
    autoprefixer: {
      cascade: false
    }
  }
})
=======
'use strict'

module.exports = (ctx) => ({
  map: ctx.file.dirname.includes('examples') ? false : {
    inline: false,
    annotation: true,
    sourcesContent: true
  },
  plugins: {
    autoprefixer: {
      cascade: false
    }
  }
})
>>>>>>> 1a1ecd553fc320897f03b38db9609eb13cd29bc3
