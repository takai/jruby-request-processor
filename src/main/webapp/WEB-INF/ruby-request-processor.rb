require 'java'

class RubyHelloAction
  def execute(mapping, form, req, res)
    req.set_attribute 'name', form.get_name()
    mapping.find_forward 'success'
  end
end

class RubyActionRequestProcessor
  def process_action_perform req, res, action, form, mapping
    HelloAction.new.execute mapping, form, req, res
  end
end

return RubyActionRequestProcessor.new