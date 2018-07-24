package uk.ac.lancaster.scc.turtles.clouseau.generator

package class ProtocolPrinter {
	
	Protocol protocol
	
	new(Protocol protocol) {
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
		'''«FOR message : protocol.getMessagesGroupedByName() SEPARATOR '\n'»«message»«ENDFOR»
		'''
	}
	
}