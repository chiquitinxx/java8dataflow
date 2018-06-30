package yila.experiments.yum

import spock.lang.Specification

class RequestSpec extends Specification {

    def 'read basic get'() {
        when:
        Request request = Request.fromHttp(basicGet, null)

        then:
        request.action == Action.GET
        request.url == '/any'
        !request.params
        request.headers == [
                'User-Agent': 'Java/1.8.0_60',
                'Host': 'localhost:5015',
                'Accept': 'text/html, image/gif, image/jpeg, *; q=.2, */*; q=.2',
                'Connection': 'keep-alive',
        ]
        !request.hasParsingErrors()
    }

    private String basicGet = '''GET /any HTTP/1.1
User-Agent: Java/1.8.0_60
Host: localhost:5015
Accept: text/html, image/gif, image/jpeg, *; q=.2, */*; q=.2
Connection: keep-alive


'''

}
