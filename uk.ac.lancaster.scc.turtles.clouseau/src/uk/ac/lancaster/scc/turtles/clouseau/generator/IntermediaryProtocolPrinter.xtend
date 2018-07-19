package uk.ac.lancaster.scc.turtles.clouseau.generator

package class IntermediaryProtocolPrinter {
	
	IntermediaryProtocol protocol
	
	new(IntermediaryProtocol protocol) {
		this.protocol = protocol
	}
	
	package def toCharSequence() {
		'''
		«protocol.getName» {
		
			«roles()»
		
			«parameters()»
		
			«messages()»
		}
		'''
	}
	
	private def roles() {
		'''role ''' +
        '''«FOR role : protocol.getRoles SEPARATOR ', '»«role»«ENDFOR»'''
	}
	
	private def parameters() {
		'''parameter'''
	}
	
	private def messages() {
		'''«FOR commitmentName : protocol.commitmentNames»
		[«commitmentName»]
		
		«FOR message : protocol.getMessagesGroupedByName(commitmentName) SEPARATOR '\n'»«message»«ENDFOR»
		
		«ENDFOR»
		'''
	}
	
}