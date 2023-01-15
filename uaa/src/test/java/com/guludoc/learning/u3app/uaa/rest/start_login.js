
const hs = response.headers.valuesOf("X-Authenticate");
var code = ""
for (var i=0; i<hs.length; i++) {
    var h = hs[i].trim();
    if(h.startsWith("realm=")) {
        code = h.replace("realm=", "");
        client.global.set("mfaid", code);
    }
}
