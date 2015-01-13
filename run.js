try {
    require("source-map-support").install();
} catch(err) {
}
require("./out/goog/bootstrap/nodejs.js");
require("./out/nestene_consciousness.js");
goog.require("nestene_consciousness.core");
goog.require("cljs.nodejscli");
