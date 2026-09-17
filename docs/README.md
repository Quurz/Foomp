# Foomp Documentation

This directory contains the documentation website for **Foomp**, built with [Astro](https://astro.build) and [Starlight](https://starlight.astro.build).

## 🚀 Development Setup

```bash
# Install dependencies
npm install

# Start local development server
npm run dev

# Build production static site
npm run build

# Preview production build locally
npm run preview
```

## 📂 Structure

- `src/content/docs/`: Markdown and MDX documentation pages (Guides, Architecture, Reference).
- `public/api/`: Static JavaDocs collected from the Gradle modules (`./gradlew assembleDocsForStarlight`).
