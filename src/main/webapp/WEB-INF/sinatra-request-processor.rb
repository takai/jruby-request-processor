require 'java'
require 'rubygems'

require 'jruby-rack'
require JRubyJars.jruby_rack_jar_path
require 'rack/handler/servlet'

import 'org.jruby.rack.servlet.ServletRackEnvironment'
import 'org.jruby.rack.servlet.ServletRackContext'

require 'sinatra'

$servlet_context = ServletRackContext.new SERVLET_CONTEXT

class ServletRackEnvironment
  def to_io
    getInputStream
  end
end

class SinatraHelloAction < Sinatra::Base
  get '/hello.do' do
    "Hello, Sinatra!"
  end
end

class SinatraRequestProcessor
  def initialize
    @app = SinatraHelloAction.new
  end

  def process req, res
    servlet_env = ServletRackEnvironment.new req
    env = Rack::Handler::LazyEnv.new servlet_env
    
    status, header, body = @app.call env.to_hash
  
    res.setStatus(status)
    header.each do |k, v|
      res.setHeader(k, v)
    end
    out = res.getOutputStream
    out.print(body.join)
  end
end

return SinatraRequestProcessor.new