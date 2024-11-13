const NodePolyfillPlugin = require('node-polyfill-webpack-plugin');

module.exports = function override(config) {
    config.plugins.push(new NodePolyfillPlugin());
    config.resolve.fallback = {
        ...config.resolve.fallback,
        "buffer": require.resolve("buffer/"),
        "fs": false,
        "https": require.resolve("https-browserify"),
        "http": require.resolve("stream-http"),
        "net": false,
        "path": require.resolve("path-browserify"),
        "stream": require.resolve("stream-browserify"),
        "url": require.resolve("url/"),
        "util": require.resolve("util/"),
        "zlib": require.resolve("browserify-zlib")
    };
    return config;
};
