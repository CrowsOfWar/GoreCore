package crowsofwar.gorecore.tree.test;

import java.util.List;

import crowsofwar.gorecore.chat.ChatMessage;
import crowsofwar.gorecore.tree.ArgumentList;
import crowsofwar.gorecore.tree.ArgumentOptions;
import crowsofwar.gorecore.tree.CommandCall;
import crowsofwar.gorecore.tree.IArgument;
import crowsofwar.gorecore.tree.ICommandNode;
import crowsofwar.gorecore.tree.ITypeConverter;
import crowsofwar.gorecore.tree.NodeFunctional;

public class TestUseChatSender extends NodeFunctional {
	
	private final IArgument<String> argFruit;
	
	public TestUseChatSender() {
		super("chatsender", false);
		this.argFruit = new ArgumentOptions<String>(ITypeConverter.CONVERTER_STRING, "fruit", "pineapple", "banana",
				"strawberry");
		addArguments(argFruit);
	}

	@Override
	protected ICommandNode doFunction(CommandCall call, List<String> options) {
		ArgumentList args = call.popArguments(argFruit);
		String fruit = args.get(argFruit);
		TestChatMessages.MESSAGE_FRUIT.send(call.getFrom(), fruit);
		return null;
	}

	@Override
	public ChatMessage getInfoMessage() {
		return TestMessages.MSG_CHATSENDER_HELP;
	}
	
}
