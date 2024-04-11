<<<<<<< HEAD
module.exports = {
	release: {
		options: {
			insertType: "prepend",
			template: "## Release v<%= package.version %>:\n\n{{> features}}{{> fixes}}\n\n",
			partials: {
				features: "#### Features:\n\n{{#if features}}{{#each features}}{{> feature}}{{/each}}{{else}}{{> empty}}{{/if}}\n",
				fixes: "#### Bugfixes:\n\n{{#if fixes}}{{#each fixes}}{{> fix}}{{/each}}{{else}}{{> empty}}{{/if}}",
				empty: " - (none)\n"
			},
			dest: "CHANGELOG.md"
		}
	}
=======
module.exports = {
	release: {
		options: {
			insertType: "prepend",
			template: "## Release v<%= package.version %>:\n\n{{> features}}{{> fixes}}\n\n",
			partials: {
				features: "#### Features:\n\n{{#if features}}{{#each features}}{{> feature}}{{/each}}{{else}}{{> empty}}{{/if}}\n",
				fixes: "#### Bugfixes:\n\n{{#if fixes}}{{#each fixes}}{{> fix}}{{/each}}{{else}}{{> empty}}{{/if}}",
				empty: " - (none)\n"
			},
			dest: "CHANGELOG.md"
		}
	}
>>>>>>> 1a1ecd553fc320897f03b38db9609eb13cd29bc3
};