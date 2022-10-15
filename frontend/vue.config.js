const path = require("path")

module.exports = {
    outputDir: path.resolve(__dirname, "E:\\WebProjects\\git-repositories\\loah8\\ck3-converter\\"),
    publicPath: '/ck3-converter/',
    filenameHashing: false,
    pluginOptions : {
        sitemap: {
            urls : [
                'https://loah8.github.io/ck3-converter/'
            ],
            pretty: true,
            outputDir: 'public/'
        }
    }
}