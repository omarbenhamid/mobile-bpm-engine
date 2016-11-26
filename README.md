# process-engine
Mobile Process Engine

This is a Process Engine that supports the execution of BPMN process on Android devices.
Given a process definition with necessary annotation, this process engine manages the execution flow and task invocation.

Manages bpmn:task / bpmn:startEvent and bpmn:endEvent who occur in in bpmn:subProcess

Processes the following extension in the above tab :

The class is the activity to start.
if spawned is set to true : we do not expect the activity to return a result and the process go on when activity finishes ok.

<bpmn:extensionElements xmlns:bpmn="http://www.omg.org/spec/BPMN/20100524/MODEL">
    <mpe:service class="it.unitn.disi.peng.process.engine.service.FormService" spawned="true" xmlns:mpe="http://peng.disi.unitn.it">
        <mpe:input id="someID" type="text|hidden|submit" value="some value"/> <!-- HTML Style -->
        <mpe:text id="someId" value="some value"/> <!-- Display some text -->
        <mpe:select id="someID">
            <mpe:option value="V1">Option 1</mpe:option>
            <mpe:option value="V1">Option 1</mpe:option>
        </mpe:select>
    </mpe:service>
</bpmn:extensionElements>

The value can be a literal string or @var for variable var or @var1\attr1\subattr1 to read subattr of attr of var object ...
or be a text with {@var} to dereference var

FIXME: select does not seam to have value!?

Or
<bpmn:extensionElements xmlns:bpmn="http://www.omg.org/spec/BPMN/20100524/MODEL">
    <mpe:service class="it.unitn.disi.peng.process.engine.service.EmailService" xmlns:mpe="http://peng.disi.unitn.it">
        <mpe:email>toto@mail.com</mpe:email>
        <mpe:subject>XXXX</mpe:subject>
        <mpe:body>XXXX</mpe:body>
    </mpe:service>
</bpmn:extensionElements>

For email, any value is either the static value or @var to replace with content of variable var.


FIXME: how does exclusive gateway work?

FIXME: each task must have a service ... should have a void service ...

fixme: sHOuld replace getNodeName by getLocalName / getNameSpace URI: especially interpreting mpe:xxx tags

Place sample.bpmn in root of your device