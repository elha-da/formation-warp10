const path = require("path");
module.exports = {
  entry: {
    "app": "./src/main/ts/main.ts"
  },
  output: {
    path: path.resolve(__dirname, "src/main/resources/front"),
    filename: "main.js"
  },
  devtool: "source-map",
  module: {
    rules: [
      {
        test: /\.ts(x?)$/,
        exclude: /node_modules/,
        use: [{
          loader: 'ts-loader'
        }]
      },
      {
        enforce: 'pre',
        test: /\.tsx?$/,
        use: "source-map-loader"
      }
    ]
  },
  resolve: {
    modules: [
      path.join(process.cwd(), "app"),
      "node_modules"
    ],
    extensions: [".js", ".json", ".ts", ".tsx"]
  }
};