from flask import Blueprint, request, jsonify
from models.site import SiteModel

site_bp = Blueprint('site', __name__)
site_model = SiteModel()

@site_bp.route('/api/site', methods=['GET'])
def get_site():
    """Liste tous les site"""
    site = site_model.get_all()
    return jsonify(site)

@site_bp.route('/api/site/<n_site>', methods=['GET'])
def get_site_by_id(n_site):
    """Récupère un site par son numéro"""
    site = site_model.get_by_id(n_site)
    if site:
        return jsonify(site)
    return jsonify({'error': 'Site non trouvé'}), 404

@site_bp.route('/api/site', methods=['POST'])
def create_site():
    """Crée un nouveau site"""
    data = request.json
    result = site_model.create(
        data.get('n_site'),
        data.get('nom'),
        data.get('lieu'),
        data.get('tarif_journalier')
    )
    if result['success']:
        return jsonify(result), 201
    return jsonify(result), 400

@site_bp.route('/api/site/<n_site>', methods=['PUT'])
def update_site(n_site):
    """Met à jour un site"""
    data = request.json
    result = site_model.update(
        n_site,
        data.get('n_site'),
        data.get('nom'),
        data.get('lieu'),
        data.get('tarif_journalier')
    )
    if result['success']:
        return jsonify(result)
    return jsonify(result), 400

@site_bp.route('/api/site/<n_site>', methods=['DELETE'])
def delete_site(n_site):
    """Supprime un site"""
    result = site_model.delete(n_site)
    if result['success']:
        return jsonify(result)
    return jsonify(result), 400
