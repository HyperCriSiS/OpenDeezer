# OpenFreezer Roadmap

> **Source of truth for OpenFreezer development.**
>
> Updated: 2026-08-23

## Project goal

OpenFreezer is a maintained, privacy-respecting, open-source Deezer client built on the OpenDeezer engine, with first-class Android support and feature parity driven by the capabilities actually exposed by Deezer rather than by a fixed clone of the official UI.

The project should keep the existing strengths of OpenDeezer — shared Go engine, native frontends, playback/download entitlement checks, reproducible builds, Connect/remote control and broad platform support — while substantially improving Deezer API coverage, discoverability, configurability and long-term maintainability.

## Development rules

- `main` is the stable branch; integration work happens on `dev` or short-lived topic branches.
- This file is the authoritative project backlog. Keep status and ordering current when work lands.
- Do not couple UI code directly to Deezer's private endpoints. Gateway/Pipe/REST details belong behind typed engine adapters.
- A capability available only for some countries, account tiers or experiments must be detected at runtime and hidden/disabled gracefully.
- Playback and downloads must continue to respect the rights/entitlements returned for the signed-in account.
- New API parsing, queue, storage and playback behavior needs automated tests.
- Never log ARL, JWT, media tokens or other session secrets. Recorded API fixtures must be sanitized.
- Prefer server-driven capabilities and feature detection over hard-coded assumptions.
- Keep all native frontends on one shared capability model; Android may lead implementation, but core features must not become Android-only by accident.

## Status legend

- [x] done
- [~] in progress / partially available
- [ ] planned

---

# M0 — Foundation, maintenance and fork bootstrap

## Dependencies and build toolchains

- [~] Move Go toolchain from 1.25.12 to the patched 1.25.13 security release.
- [~] Update direct Go dependencies and run `go mod tidy`.
- [~] Update Android Gradle Plugin / Gradle / Kotlin / Compose to current stable releases as one tested toolchain set.
- [~] Update AndroidX and image-loading dependencies to current stable releases.
- [~] Update GitHub Actions to their current supported majors, including Node 24-based releases where applicable.
- [~] Keep F-Droid/reproducible build pins synchronized with the Android toolchain.
- [~] Group Dependabot updates for Go, Gradle and GitHub Actions to avoid one CI storm per dependency.
- [ ] Make `govulncheck`, race tests, lint, Android build and reproducibility checks green after the upgrade.
- [ ] Add an explicit dependency/toolchain compatibility table to developer documentation.

## Fork identity / migration

- [ ] Rebrand user-visible `OpenDeezer` strings to `OpenFreezer` across native clients, docs and generated release assets.
- [ ] Audit update-check URLs, package metadata, website links, Connect discovery names, MCP/control API identification and release scripts before changing identifiers.
- [ ] Decide separately whether package/application IDs should change. Preserve existing IDs initially if changing them would break upgrades, app data or F-Droid continuity.
- [ ] Preserve upstream attribution and AGPL licensing notices.
- [ ] Document the upstream-sync policy and make upstream merges/rebases deliberate rather than automatic.

## CI efficiency

- [ ] Add `concurrency` + `cancel-in-progress` to PR workflows where safe.
- [ ] Add path filters so Android/F-Droid work does not run for unrelated documentation/core-only changes.
- [ ] Avoid expensive AI-review/Fuzz/F-Droid jobs on routine Dependabot changes unless the affected paths require them.
- [ ] Keep full release/reproducibility checks for tags, release candidates and relevant build-system changes.
- [ ] Measure required checks after cleanup and remove duplicate builds that prove the same thing.

---

# M1 — Deezer API coverage layer

The long-term objective is **measurable API coverage**, not a manually maintained list that becomes stale.

## Capability inventory and schema tracking

- [ ] Add a dedicated `internal/deezer/pipe` GraphQL layer alongside the existing Gateway/REST functionality.
- [ ] Fetch and sanitize Deezer Pipe schema introspection in a maintainer tool; store a reviewed schema snapshot in the repository when useful.
- [ ] Generate a machine-readable capability inventory from Pipe + Gateway + public REST endpoints.
- [ ] Generate an `implemented / partial / missing / intentionally unsupported` coverage report during maintenance work.
- [ ] Detect schema additions/removals and surface them as a controlled CI/reporting signal rather than silently breaking at runtime.
- [ ] Add typed query/mutation models or generated Go bindings where that reduces brittle hand-written JSON parsing.
- [ ] Add fixture-based parser/contract tests for every API domain. Never require a real account for ordinary CI.
- [ ] Add opt-in live integration tests for maintainers, with credentials supplied only through secrets.

## Authentication and resilience

- [ ] Implement ARL -> short-lived Pipe JWT acquisition/refresh behind the API adapter.
- [ ] Serialize concurrent JWT refreshes so one expiry cannot trigger a refresh storm.
- [ ] Distinguish invalid session/auth failures from transient server/network failures.
- [ ] Support partial GraphQL responses without discarding valid data when one item fails.
- [ ] Add bounded retry/backoff, timeout, pagination and cancellation policies shared by API calls.
- [ ] Add API response caching with explicit TTL/invalidation rules where appropriate.
- [ ] Handle schema/experiment variants defensively and retain Gateway/REST fallbacks where useful.

---

# M2 — Fully configurable Home

The Home screen should be a user-owned dashboard rather than a fixed page.

## Shared Home model

- [ ] Define a versioned, platform-neutral Home layout schema in the shared/core layer.
- [ ] Persist the Home configuration and migrate it safely when modules or layout options change.
- [ ] Build a catalog of available Home modules from both Deezer's server-driven Home/channel data and OpenFreezer-local modules.
- [ ] Allow every module to be enabled/disabled independently.
- [ ] Allow drag-and-drop reordering of all modules.
- [ ] Allow per-module item limits and optional `Show all` behavior.
- [ ] Allow per-module density/size settings where the content type supports it.
- [ ] Add `Reset to OpenFreezer default` and `Reset to Deezer/default order` actions.
- [ ] Keep unknown/new server sections non-fatal and make them eligible for later support.

## Per-section presentation

Where meaningful, every Home section should support more than one representation:

- [ ] Compact list.
- [ ] Detailed list.
- [ ] Horizontal card rail.
- [ ] Small grid.
- [ ] Large grid.
- [ ] Hero/highlight card.
- [ ] Large-card/editorial presentation.
- [ ] Mixed/highlight layout for server-driven editorial content.
- [ ] Adaptive layout choice for phone/tablet/foldable/TV while retaining the user's semantic preference.

## Home edit experience

- [ ] Add a dedicated edit mode with live preview.
- [ ] Reuse the existing Android drag-reorder infrastructure where appropriate.
- [ ] Make hidden modules discoverable in an `Add section` catalog rather than deleting their settings.
- [ ] Allow one-tap hide/move/configure from a section overflow menu.
- [ ] Add import/export/backup of Home layout with the general settings backup.

## Candidate Home modules

- [ ] Search / quick search.
- [ ] Configurable shortcuts/quick actions.
- [ ] Default Flow and all available Flow configurations.
- [ ] Made for Me / SmartTracklists.
- [ ] Personalized recommendations.
- [ ] Recently played / continue listening.
- [ ] Personal charts/top tracks/top artists/top albums.
- [ ] Favorite tracks/albums/artists/playlists/podcasts/audiobooks.
- [ ] User playlists and followed playlists as separate optional modules.
- [ ] New releases.
- [ ] Charts by country/region.
- [ ] Artist/track mixes and radios.
- [ ] Podcasts / bookmarked episodes / new episodes.
- [ ] Audiobooks / continue audiobook.
- [ ] Livestream/radio stations.
- [ ] Genre, mood and editorial channels.
- [ ] Music Together groups where available.
- [ ] Deezer server-driven Home sections not covered by the static list above.
- [ ] Local download/offline shortcuts and recently downloaded content.

---

# M3 — Search and discovery parity

- [ ] Replace/augment the simple REST search with Pipe unified/instant search.
- [ ] Support a best/top result plus mixed results.
- [ ] Search tracks, artists, albums, playlists, podcasts, podcast episodes, livestreams and supported audiobook entities.
- [ ] Discover/search Flow configurations.
- [ ] Add search suggestions/autocomplete.
- [ ] Add account search history / recent searches with clear/remove controls.
- [ ] Support Deezer search landing/discovery channels.
- [ ] Add category tabs and useful filters without forcing users through separate searches.
- [ ] Add pagination/infinite loading for large result sets.
- [ ] Add deep links/open-by-ID for supported Deezer entities.
- [ ] Where Deezer expects it, report successful search-result selection without leaking unrelated telemetry.

## Channels / editorial discovery

- [ ] Implement a generic server-driven Deezer page/channel model rather than hard-coding every editorial page.
- [ ] Support common section item types: track, album, artist, playlist, user, Flow, smart tracklist, podcast/show, radio/livestream, channel and supported rich cards.
- [ ] Support common server layouts such as grid, horizontal grid, filterable grid, highlight and large card.
- [ ] Add genre/mood/theme pages.
- [ ] Add regional charts/editorial selection.
- [ ] Gracefully skip unsupported promotional/app/video card types rather than failing a page.

---

# M4 — Library parity and playlist management

## Favorites

- [x] Like/unlike tracks.
- [ ] Favorite/unfavorite albums.
- [ ] Follow/unfollow artists.
- [ ] Favorite/unfavorite playlists.
- [ ] Follow/unfollow podcasts.
- [ ] Favorite/unfavorite audiobooks when the account/API exposes it.
- [ ] Present each favorite type as a first-class Library category.
- [ ] Add Library search, filters and configurable sorting.
- [ ] Add `downloaded/offline only` views.
- [ ] Add shuffle/play-all for compatible Library collections.

## Playlists

- [x] Create/rename/delete playlists at basic level.
- [x] Add tracks to playlists at basic level.
- [ ] Remove tracks through the modern API path with robust error recovery.
- [ ] Reorder playlist tracks when supported.
- [ ] Edit description, visibility/privacy and collaborative state where Deezer allows it.
- [ ] Upload/change playlist artwork.
- [ ] Show owner, collaborators, fan/favorite state and richer metadata.
- [ ] Add duplicate-track warning as an optional UX feature.
- [ ] Add multi-select/bulk actions in long lists.

---

# M5 — Personalized playback, Flow, radio and recommendations

- [x] Basic default Flow.
- [x] Basic track and artist mix support in the engine.
- [ ] Expose all available Flow configurations (mood/genre/etc.) with their Deezer-provided title/artwork.
- [ ] Support batched/fresh Flow loading and continuous replenishment.
- [ ] Implement Made for Me collections and SmartTracklists.
- [ ] Implement personalized recommendations as native entities, not only Home decorations.
- [ ] Implement similar-track recommendations.
- [ ] Complete artist mix / track mix entry points throughout the UIs.
- [ ] Add artist radio/smart radio where exposed.
- [ ] Add optional context-aware autoplay after the explicit queue ends.
- [ ] Preserve source/context metadata in the queue so continuation behaves correctly after restart/Connect transfer.

---

# M6 — Complete entity pages

## Artist

- [x] Basic artist profile, top tracks and related content.
- [ ] Full discography grouped into Albums / EPs / Singles and additional server-provided release types.
- [ ] Distinguish main/featured/other contributor roles where available.
- [ ] Favorite/follow state and action.
- [ ] Artist radio/mix shortcut.
- [ ] Biography and richer metadata with graceful regional fallbacks.

## Album

- [ ] Rich metadata: contributors, label/publisher, copyright, release date, explicit state, fans/favorite state and availability.
- [ ] Favorite/unfavorite action.
- [ ] Disc-aware presentation and robust multi-disc ordering.
- [ ] Related/recommended content when exposed.

## Track

- [ ] Rich detail sheet: ISRC, contributors, album/disc data, explicit state, quality/rights and other useful metadata.
- [ ] Similar tracks / start track mix.
- [ ] Complete synced lyrics support including word-level timing when supplied by Deezer.
- [ ] Credits/writers/copyright where supplied.

---

# M7 — Podcasts, audiobooks and livestreams

## Podcasts

- [x] Basic podcast search/listing/playback.
- [ ] Favorite/follow podcasts.
- [ ] Podcast episode bookmarks.
- [ ] Mark episode played/unplayed.
- [ ] Continue-listening progress with reliable resume.
- [ ] Episode details and publication metadata.
- [ ] New-episode notification subscription when the Gateway capability remains available.
- [ ] Download/offline management optimized for episodic content.

## Audiobooks

- [ ] Add Audiobook as a first-class entity rather than treating it as a normal album.
- [ ] Audiobook search/discovery where returned by Deezer.
- [ ] Audiobook detail with description, publisher/contributors, chapter/disc counts and duration.
- [ ] Chapter playback with resume position.
- [ ] Favorite/library integration where available.
- [ ] Offline chapter/book download management.
- [ ] Separate `Continue audiobook` Home module.

## Livestreams / radio

- [ ] Search and open Deezer livestream entities.
- [ ] Display station name, language, country, description, artwork and current availability where exposed.
- [ ] Robust external-media playback path for livestream URLs/codecs.
- [ ] Add livestream/radio Home and channel modules.

---

# M8 — Music Together and social-capability experiments

- [ ] Implement Deezer Music Together capability detection.
- [ ] List the user's groups and group details.
- [ ] Create/join/leave groups.
- [ ] Display members and group readiness/family metadata where available.
- [ ] Fetch curated and suggested group tracklists.
- [ ] Support mood selection/refresh for suggested tracks.
- [ ] Update group settings and generated names where supported.
- [ ] Show affinity information only when useful and clearly explain what it represents.
- [ ] Keep this feature fully optional and absent when Deezer does not expose it for the account/region.

---

# M9 — Account, notifications and history

- [ ] Replace local-only history where appropriate with Deezer `recentlyPlayed` data, while retaining an optional local history for privacy/offline use.
- [ ] Add personal user charts (top tracks/artists/albums) where exposed.
- [ ] Add a notification center for supported Deezer account/app notifications.
- [ ] Add unread/read handling only if the API semantics are understood and safe.
- [ ] Add account/profile display and editable fields that Deezer actually accepts.
- [ ] Add profile image update if the upload API remains supported.
- [ ] Make session expiry/re-login/switch-account behavior consistent across all frontends.
- [ ] Consider multiple saved accounts with encrypted/local credential handling; never sync ARLs through plain settings backup.

---

# M10 — Playback, Android Auto and platform media integration

## Shared playback

- [x] Queue, shuffle/repeat, seek/resume, gapless, crossfade, ReplayGain and output-device selection exist in the shared engine.
- [ ] Add regression tests for queue restore, end-of-track transitions, gapless/crossfade boundaries and failed media resolution.
- [ ] Harden network interruption/reconnect and account-expiry recovery during playback.
- [ ] Audit free-tier ad/reporting behavior and retain explicit user-facing semantics.
- [ ] Persist source/context information required for Flow/SmartTracklist/autoplay continuation.

## Android

- [x] Background PlaybackService and MediaSession exist.
- [ ] Audit Android audio focus, noisy-device events, Bluetooth changes, calls and process recreation on current Android releases.
- [ ] Verify full Android Auto browse/search/catalog integration. A MediaSession alone is not considered Android Auto feature completion.
- [ ] Add Android Auto browse hierarchy for Home/Library/Flow/Playlists/Podcasts where safe and useful.
- [ ] Evaluate migration from legacy `androidx.media` to Media3 after the dependency/toolchain migration is stable.
- [ ] Polish Android TV focus/navigation and remote-control behavior.
- [ ] Validate phone/tablet/foldable adaptive layouts after the Compose upgrade.

---

# M11 — Downloads, cache and offline mode

- [x] Basic track/album/playlist downloading and encrypted offline cache exist.
- [ ] Unified download manager with queue, progress, retry, cancel and clear error states.
- [ ] Pause/resume where technically safe.
- [ ] Download-state indicators throughout Library/Home/entity lists.
- [ ] `Download missing tracks` / `Remove download` actions for collections.
- [ ] Storage quota and cache cleanup policies.
- [ ] Separate temporary stream cache from user-requested offline downloads.
- [ ] Android storage-location/SAF evaluation without weakening scoped-storage behavior.
- [ ] Detect removed/expired rights and explain unavailable offline items cleanly.
- [ ] Robust metadata/artwork tagging for exported downloads.
- [ ] Verify downloaded podcast/audiobook resume behavior separately from music tracks.

---

# M12 — UX and power-user improvements

- [ ] Configurable primary navigation: enable/disable/reorder tabs while keeping essential destinations reachable.
- [ ] Material 3 dynamic color + optional fixed accent + AMOLED-black mode on Android.
- [ ] Consistent compact/detailed/card/grid display preferences for Library/Search where useful.
- [ ] Multi-select mode and bulk actions.
- [ ] Better context menus with Play next / Add to queue / Start mix / Add to playlist / Download / Share / Favorite.
- [ ] Queue `Play next` and `Add to end` semantics everywhere.
- [ ] Local `Play later`/bookmarks collection independent of Deezer favorites.
- [ ] Local smart playlists based on favorite/download/history metadata without pretending they are server playlists.
- [ ] Optional privacy-local listening statistics/dashboard.
- [ ] Android widgets and launcher shortcuts for play/pause, Flow, favorites and recent content.
- [ ] Share/deep-link handling for Deezer URLs into the native apps.
- [ ] Settings search.
- [ ] Backup/restore for non-secret preferences, Home layout, navigation layout and local smart collections.
- [ ] Accessibility audit: TalkBack/VoiceOver labels, focus order, contrast, scalable text and reduced-motion behavior.

---

# M13 — Import/export and interoperability

- [~] Existing M3U/export infrastructure: audit current behavior and expose it consistently in native UIs.
- [ ] M3U import with dry-run/match report.
- [ ] Export playlists/library references in portable formats without embedding account secrets.
- [ ] Evaluate Spotify playlist import using user-authorized/public metadata only; keep it modular and optional.
- [ ] Export/import OpenFreezer non-secret settings and layout configuration.
- [ ] Keep OpenDeezer Connect/control API backward-compatible where practical and version it before breaking changes.

---

# M14 — Cross-platform feature parity

- [ ] Maintain a generated/manual capability matrix for TUI, Android, Android TV, iOS, Windows, macOS, GNOME and KDE.
- [ ] Core/API capability lands first; each frontend should expose it or explicitly mark it unsupported.
- [ ] Avoid duplicating API business logic in Swift/Kotlin/C#/Qt/GTK frontends.
- [ ] Share Home configuration semantics across platforms even if exact visual components differ.
- [ ] Add platform smoke tests for startup/login/navigation/playback where automation is practical.
- [ ] Keep Connect/remote state behavior consistent across clients.

---

# M15 — Security, quality and observability

- [x] Race testing, linting, fuzz harnesses and ClusterFuzzLite foundations exist.
- [ ] Keep `govulncheck` clean on the pinned Go release.
- [ ] Add secret scanning for generated fixtures/test data before commit.
- [ ] Add targeted fuzzing for GraphQL/Gateway response decoding, media manifests, metadata/tag parsing and Connect/control messages.
- [ ] Add redacted structured diagnostics users can export for bug reports.
- [ ] Separate user-facing errors (auth, rights, network, decoding, storage) instead of generic failures.
- [ ] Never include tokens/ARL/media URLs with credentials in diagnostics.
- [ ] Harden embedded WebView login navigation/domain allowlists.
- [ ] Continue reproducible Android/F-Droid builds and signed-release checksum/provenance checks.
- [ ] Audit third-party dependencies and remove unused libraries as the architecture stabilizes.

---

# Suggested execution order

1. **M0** — finish dependency/toolchain migration, green CI, fork identity plan.
2. **M1** — typed Pipe/API capability layer + automated coverage report.
3. **M2** — configurable Home powered by the new generic page/capability model.
4. **M3 + M4** — unified search/discovery and complete Library/favorites/playlist operations.
5. **M5 + M6** — Flow/recommendations/mixes and complete entity pages.
6. **M7 + M9** — podcasts, audiobooks, livestreams, account/history/notifications.
7. **M8** — Music Together as an optional capability-gated feature.
8. **M10 + M11** — media integration, Android Auto, downloads/offline hardening.
9. **M12–M15** — UX, interoperability, cross-platform parity, security and polish continuously alongside feature work.

## Definition of API parity

OpenFreezer does **not** claim parity merely because a hand-written checklist is complete. API parity means:

1. the current Deezer Pipe/Gateway/public API capability inventory has been refreshed,
2. every relevant consumer-facing capability is classified as implemented, partial, unavailable for the current account/region, or intentionally unsupported,
3. missing relevant capabilities are represented in this roadmap or a generated coverage artifact,
4. schema/API drift produces a visible maintenance signal,
5. supported operations have fixture/contract tests and fail gracefully when Deezer changes an experiment or response shape.

This keeps the roadmap aligned with Deezer as the service evolves instead of freezing OpenFreezer at the feature set visible on 2026-08-23.
