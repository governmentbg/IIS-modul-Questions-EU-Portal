<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;

/**
 * @property int        $A_ns_MP_Mt_id
 * @property int        $A_ns_MP_id
 * @property Date       $A_ns_MP_Mt_date
 * @property string     $A_ns_MP_Mt_body
 * @property int        $created_at
 * @property int        $updated_at
 * @property int        $deleted_at
 */
class MPMeetings extends Model
{
    use SoftDeletes;
    /**
     * The database table used by the model.
     *
     * @var string
     */
    protected $table = 'MP_Meetings';

    /**
     * The primary key for the model.
     *
     * @var string
     */
    protected $primaryKey = 'A_ns_MP_Mt_id';

    /**
     * Attributes that should be mass-assignable.
     *
     * @var array
     */
    protected $fillable = [
        'A_ns_MP_Mt_id', 'A_ns_MP_id', 'A_ns_MP_Mt_date', 'A_ns_MP_Mt_body', 'created_at', 'updated_at', 'deleted_at'
    ];

    /**
     * The attributes excluded from the model's JSON form.
     *
     * @var array
     */
    protected $hidden = [];

    /**
     * The attributes that should be casted to native types.
     *
     * @var array
     */
    protected $casts = [
        'A_ns_MP_Mt_id' => 'int', 'A_ns_MP_id' => 'int', 'A_ns_MP_Mt_date' => 'date', 'A_ns_MP_Mt_body' => 'string', 'created_at' => 'timestamp', 'updated_at' => 'timestamp', 'deleted_at' => 'timestamp'
    ];

    /**
     * The attributes that should be mutated to dates.
     *
     * @var array
     */
    protected $dates = [
        'A_ns_MP_Mt_date', 'created_at', 'updated_at', 'deleted_at'
    ];

    /**
     * Indicates if the model should be timestamped.
     *
     * @var boolean
     */
    public $timestamps = false;

    public static function boot()
    {
        parent::boot();

        static::creating(function ($article) {
            $article->created_at = now();
            $article->updated_at = now();
        });

        static::saving(function ($article) {
            $article->updated_at = now();
        });
    }

    // Scopes...

    // Functions ...

    // Relations ...

    public function eq_meetings()
    {
        return $this->belongsTo(ANsMps::class, 'A_ns_MP_id');
    }
}
