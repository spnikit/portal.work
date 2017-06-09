function anonymous(n, nsr) {
	with (XPath) {
		return (axis(axes["child"], function(n) {
					return n.nodeType == 1
				}, [n]), nl.length || 0)
	}
}
