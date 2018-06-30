package yila.experiments.yum

import org.apache.http.NameValuePair
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.CloseableHttpResponse
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClients
import org.apache.http.message.BasicNameValuePair
import spock.lang.Specification
import spock.util.concurrent.PollingConditions

class ServerSpec extends Specification {

    def 'starts the server and receive result as json'() {
        when:
        def result = getUrl('/any')
        def headers = result.allHeaders.toList()

        then:
        result.statusLine.statusCode == 200
        result.statusLine.protocolVersion.toString() == 'HTTP/1.1'
        result.entity.content.text == '{"hello": "world", "number": 1200, "bigd": 1.57}'
        headers.size() == 4
        headers.any { it.name == 'Content-Type' && it.value == 'application/json; charset=UTF-8' }
        headers.any { it.name == 'Server' && it.value == 'Yum' }
        headers.any { it.name == 'Content-Length' && it.value == '48' }
        headers.any { it.name == 'Connection' && it.value == 'close' }
        !result.entity.chunked
    }

    def 'get only text'() {
        when:
        def result = getUrl('/text')
        def headers = result.allHeaders.toList()

        then:
        result.statusLine.statusCode == 200
        result.entity.content.text == 'hola'
        headers.size() == 4
        headers.any { it.name == 'Content-Type' && it.value == 'text/plain' }
        headers.any { it.name == 'Server' && it.value == 'Yum' }
        headers.any { it.name == 'Content-Length' && it.value == '4' }
        headers.any { it.name == 'Connection' && it.value == 'close' }
    }

    def 'returns a 404 if no match url'() {
        when:
        def result = getUrl('/none')

        then:
        result.statusLine.statusCode == 404
    }

    def 'define an input interceptor'() {
        given:
        def number = 0
        def conditions = new PollingConditions()

        when:
        server.addInputInterceptor({ request -> number++ })
        2.times { it -> getUrl('/any') }

        then:
        conditions.eventually {
            assert number == 2
        }
    }

    def 'post with form data in the body'() {
        when:
        def result = postForm('/post', [one: "1", two: "2"])

        then:
        result.statusLine.statusCode == 200
    }

    def setup() {
        server = newServer
        httpclient = HttpClients.createDefault()
    }

    def cleanup() {
        httpclient.close()
        server.stop()
    }

    private final int port = 5015
    private CloseableHttpClient httpclient
    private Server server

    private Server getNewServer() {
        Server.start(port).on('/any', { Request request ->
            request.sendResponse(200, [
                    'hello': 'world',
                    'number': 1200,
                    'bigd': 1.57
            ])
        }).on('/text', { Request request ->
            request.sendResponse(200, 'hola')
        }).on(Action.POST, '/post', { Request request ->
            if (request.params['one'] == '1' && request.params['two'] == '2') {
                request.sendResponse(200)
            } else {
                request.sendResponse(400)
            }
        })
    }

    private CloseableHttpResponse getUrl(String path) {
        HttpGet httpGet = new HttpGet(getLocalUrl(path))
        httpclient.execute(httpGet)
    }

    private CloseableHttpResponse postForm(String path, Map<String, String> map) {
        HttpPost httpPost = new HttpPost(getLocalUrl(path))
        List <NameValuePair> pairs = new ArrayList <NameValuePair>()
        map.each { key, value ->
            pairs.add(new BasicNameValuePair(key, value))
        }
        httpPost.setEntity(new UrlEncodedFormEntity(pairs))
        httpclient.execute(httpPost)
    }

    private getLocalUrl(String path) {
        "http://localhost:${port}${path}"
    }
}
