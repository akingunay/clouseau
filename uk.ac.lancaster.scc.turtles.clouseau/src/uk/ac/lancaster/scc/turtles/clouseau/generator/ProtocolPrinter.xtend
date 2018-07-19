package uk.ac.lancaster.scc.turtles.clouseau.generator

package class ProtocolPrinter {
	
	Protocol protocol
	
	new(Protocol protocol) {
		this.protocol = protocol
	}
	
	package def toCharSequence() {
		'''
		«protocol.name» {
		
			«roles()»
		
			«parameters()»
		
			«messages()»
		}
		'''
	}
	
	private def roles() {
		'''role ''' +
        '''«FOR role : protocol.roles SEPARATOR ', '»«role»«ENDFOR»'''
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