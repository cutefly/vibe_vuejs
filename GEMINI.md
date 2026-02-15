# GEMINI.md

This file contains project-specific context and guidelines for AI assistants (like Gemini) working on this codebase.

## Project Overview

This is a TypeScript-based web application built with:
- **Node.js**: v24
- **Vue.js**: v3 (Composition API)
- **TypeScript**: Latest stable version
- **Build Tool**: Vite (recommended) or Vue CLI

## Tech Stack

### Core
- Vue 3 with Composition API and `<script setup>` syntax
- TypeScript for type safety
- Vite for fast development and optimized builds

### Recommended Libraries
- **State Management**: Pinia (Vue 3 official state management)
- **Routing**: Vue Router v4
- **HTTP Client**: Axios or Fetch API
- **UI Framework**: (specify your choice - e.g., Vuetify, Element Plus, PrimeVue)
- **Testing**: Vitest + Vue Test Utils

## Code Style & Conventions

### TypeScript Guidelines
- Use strict mode (`"strict": true` in tsconfig.json)
- Prefer `interface` over `type` for object shapes
- Use explicit return types for functions
- Avoid `any` type - use `unknown` when type is truly unknown
- Use type guards for runtime type checking

```typescript
// ✅ Good
interface User {
  id: number;
  name: string;
  email: string;
}

function getUser(id: number): Promise<User> {
  // implementation
}

// ❌ Avoid
function getUser(id: any): any {
  // implementation
}
```

### Vue 3 Composition API
- Use `<script setup>` syntax for cleaner component code
- Prefer `ref()` and `reactive()` for state management
- Use `computed()` for derived state
- Extract reusable logic into composables (files in `/composables` directory)

```vue
<script setup lang="ts">
import { ref, computed } from 'vue'
import type { User } from '@/types'

const user = ref<User | null>(null)
const isLoggedIn = computed(() => user.value !== null)

function login(credentials: LoginCredentials) {
  // implementation
}
</script>
```

### File Naming Conventions
- Components: PascalCase (e.g., `UserProfile.vue`, `NavBar.vue`)
- Composables: camelCase with `use` prefix (e.g., `useAuth.ts`, `useFetch.ts`)
- Types/Interfaces: PascalCase (e.g., `User.ts`, `ApiResponse.ts`)
- Utilities: camelCase (e.g., `formatDate.ts`, `validators.ts`)

### Directory Structure
```
.
├── frontend/           # Vue 3 Frontend
│   ├── src/            # Static assets (images, fonts, etc.)
│   ├── components/     # Reusable Vue components
│   ├── composables/    # Composition API composables
│   ├── layouts/        # Layout components
│   ├── pages/          # Page/view components
│   ├── router/         # Vue Router configuration
│   ├── stores/         # Pinia stores
│   ├── types/          # TypeScript type definitions
│   ├── utils/          # Utility functions
│   ├── services/       # API services
│   └── App.vue         # Root component
└── backend/            # Spring Boot Backend
    ├── src/            # Java source files
    └── pom.xml         # Maven configuration
```

## Development Guidelines

### Component Best Practices
1. Keep components focused and single-purpose
2. Use props for parent-to-child communication
3. Use events (`emit`) for child-to-parent communication
4. Use provide/inject for deep component trees
5. Implement proper TypeScript typing for props and emits

```vue
<script setup lang="ts">
interface Props {
  title: string
  count?: number
}

interface Emits {
  (e: 'update', value: number): void
  (e: 'delete'): void
}

const props = withDefaults(defineProps<Props>(), {
  count: 0
})

const emit = defineEmits<Emits>()
</script>
```

### State Management
- Use Pinia for global state
- Keep component-local state in components with `ref`/`reactive`
- Use composables for shared logic that doesn't need global state

### API Integration
- Centralize API calls in `/services` directory
- Create typed interfaces for API responses
- Implement proper error handling
- Use environment variables for API endpoints

```typescript
// services/userService.ts
import type { User } from '@/types'

export const userService = {
  async getUser(id: number): Promise<User> {
    const response = await fetch(`${import.meta.env.VITE_API_URL}/users/${id}`)
    if (!response.ok) throw new Error('Failed to fetch user')
    return response.json()
  }
}
```

### Error Handling
- Use try-catch blocks for async operations
- Provide user-friendly error messages
- Log errors appropriately for debugging
- Consider implementing a global error handler

### Performance Considerations
- Use `defineAsyncComponent` for code splitting
- Implement lazy loading for routes
- Use `v-once` for static content
- Optimize computed properties (avoid expensive calculations)
- Use `shallowRef`/`shallowReactive` when deep reactivity isn't needed

## Testing

### Unit Tests
- Test composables independently
- Test component logic (not implementation details)
- Mock external dependencies
- Aim for meaningful test coverage, not 100%

```typescript
import { describe, it, expect } from 'vitest'
import { mount } from '@vue/test-utils'
import UserCard from '@/components/UserCard.vue'

describe('UserCard', () => {
  it('renders user name correctly', () => {
    const wrapper = mount(UserCard, {
      props: { user: { id: 1, name: 'John Doe' } }
    })
    expect(wrapper.text()).toContain('John Doe')
  })
})
```

## Environment Variables

Use `.env` files for environment-specific configuration:
- `.env` - Default variables
- `.env.local` - Local overrides (git-ignored)
- `.env.production` - Production variables

Prefix variables with `VITE_` to expose them to the client:
```
VITE_API_URL=https://api.example.com
VITE_APP_NAME=My App
```

## Build & Deployment (Frontend)

Commands should be run from the `frontend/` directory.

### Development
```bash
cd frontend
npm run dev
```

### Production Build
```bash
cd frontend
npm run build
npm run preview  # Preview production build locally
```

### Type Checking
```bash
cd frontend
npm run type-check
```

## Common Patterns & Solutions

### Async Data Fetching in Components
```vue
<script setup lang="ts">
import { ref, onMounted } from 'vue'
import type { User } from '@/types'
import { userService } from '@/services/userService'

const user = ref<User | null>(null)
const loading = ref(false)
const error = ref<string | null>(null)

onMounted(async () => {
  loading.value = true
  try {
    user.value = await userService.getUser(1)
  } catch (e) {
    error.value = e instanceof Error ? e.message : 'Unknown error'
  } finally {
    loading.value = false
  }
})
</script>
```

### Form Handling with Type Safety
```vue
<script setup lang="ts">
import { reactive } from 'vue'

interface LoginForm {
  email: string
  password: string
}

const form = reactive<LoginForm>({
  email: '',
  password: ''
})

function handleSubmit() {
  // form is fully typed
  console.log(form.email, form.password)
}
</script>
```

## Node.js Version

This project requires Node.js 24. Ensure your environment uses the correct version:
```bash
node --version  # Should output v24.x.x
```

Consider using a version manager like `nvm` or `fnm`:
```bash
nvm use 24
# or
fnm use 24
```

## AI Assistant Guidelines

When working on this codebase:
1. Always use TypeScript with proper typing
2. Follow Vue 3 Composition API patterns
3. Maintain consistency with existing code style
4. Add JSDoc comments for complex functions
5. Update this GEMINI.md if you introduce new patterns or conventions
6. Ensure all code is compatible with Node 24
7. Test code changes when possible

## Resources

- [Vue 3 Documentation](https://vuejs.org/)
- [TypeScript Documentation](https://www.typescriptlang.org/)
- [Pinia Documentation](https://pinia.vuejs.org/)
- [Vite Documentation](https://vitejs.dev/)
- [Vue Router Documentation](https://router.vuejs.org/)

---

Last updated: 2026-02-13
