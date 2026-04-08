#!/bin/bash

echo "--- 🛠 Configuration du Metrics Server pour Minikube ---"

# 1. Activation de l'addon metrics-server (propre à Minikube)
minikube addons enable metrics-server

echo "Patientez 10 secondes pour le déploiement..."
sleep 10

# 2. Application du patch TLS (Indispensable pour éviter l'erreur 'Metrics API not available')
# On dit au metrics-server de ne pas vérifier les certificats du nœud Minikube
kubectl patch deployment metrics-server -n kube-system --type='json' -p='[{"op": "add", "path": "/spec/template/spec/containers/0/args/-", "value": "--kubelet-insecure-tls"}]'

echo "--- ♻️ Redémarrage du serveur de métriques ---"
kubectl rollout restart deployment metrics-server -n kube-system

echo "--- ⏳ Attente de la stabilisation (environ 45s) ---"
# On attend que l'API réponde 'Available'
kubectl wait --for=condition=Available apiservice/v1beta1.metrics.k8s.io --timeout=60s

echo "--- ✅ C'est prêt ! ---"
kubectl top nodes