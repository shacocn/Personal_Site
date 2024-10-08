module.exports = {
  devServer: {
    port: process.env.VUE_APP_PORT || 8082,
    disableHostCheck: true,
    proxy: {
      '/api': {
        target: 'http://localhost:8080', // The backend server URL
        changeOrigin: true,
        pathRewrite: { '^/api': '' },
      },
    }
  },
  lintOnSave: false,
  transpileDependencies: ['vuetify'],

  pluginOptions: {
    i18n: {
      locale: 'en',
      fallbackLocale: 'en',
      localeDir: 'locales',
      enableInSFC: false,
    },
  },
}
