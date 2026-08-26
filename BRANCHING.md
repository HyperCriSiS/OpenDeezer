# Branching workflow

`main` is the long-lived product/integration branch for this fork.

The existing `dev` branch is transitional. After its remaining work is integrated, new development should use short-lived branches from `main` and merge directly back into `main`.

Use `feature/*`, `fix/*`, `refactor/*`, `test/*`, `docs/*`, `chore/*`, and `hotfix/*`. Use `release/<version>` only for temporary release stabilization. Do not keep permanent module, provider, platform, `dev`, or `staging` branches.

Stacked pull requests are allowed only for genuine temporary dependencies. The authoritative roadmap must live on `main`, and released versions should be represented by Git tags/releases rather than permanent release branches.
