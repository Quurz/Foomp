// @ts-check
import { defineConfig } from 'astro/config';
import starlight from '@astrojs/starlight';

// https://astro.build/config
export default defineConfig({
	locales: {
		root: {
			label: 'English',
			lang: 'en',
		},
	},
	integrations: [
		starlight({
			title: 'My Docs',
			social: [{ icon: 'github', label: 'GitHub', href: 'https://github.com/withastro/starlight' }],
			sidebar: [
				{
					label: 'Guides',
					autogenerate: { directory: 'guides' },
				},
				{
					label: 'Reference',
					autogenerate: { directory: 'reference' },
				},
				{
					label: 'API Reference (JavaDoc)',
					items: [
						{ label: 'Base Module', href: '/api/base/index.html', attrs: { target: '_blank' } },
						{ label: 'Higher Module', href: '/api/higher/index.html', attrs: { target: '_blank' } },
					],
				},
			],
		}),
	],
});
